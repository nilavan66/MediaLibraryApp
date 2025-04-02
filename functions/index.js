const {onObjectFinalized} = require("firebase-functions/v2/storage");
const {initializeApp} = require("firebase-admin/app");
const {onDocumentDeleted} = require("firebase-functions/v2/firestore");
const {getFirestore, Timestamp} = require("firebase-admin/firestore");
const {getStorage} = require("firebase-admin/storage");

initializeApp();

exports.onObjectFinalized = onObjectFinalized(async (event) => {
  try {
    const object = event.data;
    const bucketName = object.bucket;
    const filePath = object.name;

    if (!filePath) {
      console.log("No file path found.");
      return;
    }

    // Get file metadata from Storage
    const file = getStorage().bucket(bucketName).file(filePath);
    const [metadata] = await file.getMetadata();

    // Extract download token (if available)
    const token = metadata.metadata.firebaseStorageDownloadTokens || null;

    const timeCreatedTimestamp = Timestamp
        .fromDate(new Date(metadata.timeCreated));

    const fileName = filePath.split("/").pop();

    // Construct the correct download URL
    let downloadUrl = `https://firebasestorage.googleapis.com/v0/b/${bucketName}/o/${encodeURIComponent(filePath)}?alt=media`;
    if (token) {
      downloadUrl += `&token=${token}`;
    }

    // Prepare metadata for Firestore
    const fileMetadata = {
      name: fileName,
      contentType: metadata.contentType,
      size: Number(metadata.size),
      timeCreated: timeCreatedTimestamp,
      downloadUrl: downloadUrl,
    };

    // Store metadata in Firestore (Auto Document ID)
    await getFirestore().collection("media_data").add(fileMetadata);

    console.log("Metadata saved to Firestore for", metadata.name);
  } catch (error) {
    console.error("Error processing file:", error);
  }
});

// handle deletion of files from Firestore and Firebase Storage
exports.onDocumentDeleted = onDocumentDeleted(async (event) => {
  try {
    console.log("Event data:", event.data);

    // Ensure event data exists
    if (!event.data) {
      console.error("No event data received.");
      return;
    }

    const deletedDoc = event.data;
    const documentId = deletedDoc.id;

    console.log(`Attempting to delete document with ID:
        ${documentId}`);

    // Ensure document data exists
    const mediaData = deletedDoc.data();
    if (!mediaData) {
      console.error(`No media data found for document ID:
           ${documentId}`);
      return;
    }

    // Ensure downloadUrl exists
    const fileUrl = mediaData.downloadUrl;
    if (!fileUrl) {
      console.error(`No download URL found in document ID:
          ${documentId}`);
      return;
    }

    console.log("File URL found:", fileUrl);

    // Extract and decode file path
    const filePath = decodeURIComponent(fileUrl.split("/o/")[1]
        .split("?alt")[0]);
    console.log("Decoded file path:", filePath);

    // Get Firebase Storage bucket
    const bucket = getStorage().bucket();
    const file = bucket.file(filePath);

    // Delete file from Firebase Storage
    await file.delete();
    console.log(`File deleted from Storage: ${filePath}`);

    await getFirestore().collection("media_data").doc(documentId).delete();
    console.log(`Document deleted from Firestore: ${documentId}`);
  } catch (error) {
    console.error("Error occurred during delete operation:", error);
  }
});


