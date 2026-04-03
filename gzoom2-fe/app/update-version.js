const fs = require('fs');
const packageJson = require('./package.json');

// Define the path to the version.js file
const versionFilePath = './src/assets/version.json';

// Create the content for the version.js file with the updated version
const versionFileContent = `{
  "name": "GZoom2",
  "version": "${packageJson.version}"
}\n`;

// Write the content to version.js
fs.writeFile(versionFilePath, versionFileContent, (err) => {
  if (err) {
    console.error(`Error updating version.js: ${err}`);
    process.exit(1);
  }
  console.log(`version.js updated to version ${packageJson.version}`);
});
