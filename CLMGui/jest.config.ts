import type {Config} from 'jest';

export default async (): Promise<Config> => {
  return {
    verbose: true,
    testEnvironment: "jsdom",
    collectCoverageFrom: [
        "src/**/*.{js,jsx,ts,tsx}",
        "!<rootDir>/node_modules/",
        "!src/index.tsx",
        "!src/reportWebVitals.ts",
        "!src/serviceWorker.ts",
        "!src/serviceWorkerRegistration.ts"
      ],
    moduleNameMapper: {
        "\\.(jpg|jpeg|png|gif|eot|otf|webp|svg|ttf|woff|woff2|mp4|webm|wav|mp3|m4a|aac|oga)$": "<rootDir>/__mocks__/fileMock.js",
        "\\.(css|less)$": "<rootDir>/__mocks__/styleMock.js"
      }
  };
};