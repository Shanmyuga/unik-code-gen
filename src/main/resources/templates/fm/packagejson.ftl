{
  "name": "react-hello",
  "version": "1.0.0",
  "description": "React Hello",
  "main": "index.js",
  "scripts": ${scripts},
  "jest": {
    "snapshotSerializers": [
      "enzyme-to-json/serializer"
    ],
    "setupFiles": [
      "./test/setupTests.js"
    ]
  },
  "author": "Cognizant",
  "license": "ISC",
  "dependencies": ${dependencies},
  "devDependencies": ${devDependencies}
}
