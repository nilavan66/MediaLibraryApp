const {onObjectFinalized} = require("firebase-functions/v2/storage");
const {initializeApp} = require("firebase-admin/app");

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

    // Construct the correct download URL
    let downloadUrl = `https://firebasestorage.googleapis.com/v0/b/${bucketName}/o/${encodeURIComponent(filePath)}?alt=media`;
    if (token) {
      downloadUrl += `&token=${token}`;
    }

    // Prepare metadata for Firestore
    const fileMetadata = {
      name: metadata.name,
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
