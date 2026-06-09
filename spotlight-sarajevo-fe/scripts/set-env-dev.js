/* When adding new env variables and copying/running the repo locally for development, we run this */
const fs = require('fs');
const dotenv = require('dotenv');
dotenv.config();

const targetPath = './src/environments/environment.ts';
const envConfigFile = `export const environment = {
    production: false,
    IMAGE_BB_API: "${process.env.IMAGE_BB_KEY}",
    API_URL: "http://localhost:8080",
    GOOGLE_CLIENT_ID: "${process.env.GOOGLE_CLIENT_ID}"
};`;

fs.writeFileSync(targetPath, envConfigFile);
