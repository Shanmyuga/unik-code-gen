# UNIK Code Gen [![Build Status](https://travis-ci.org/ngx-translate/core.svg?branch=master)](https://travis-ci.org/ngx-translate/core) [![npm version](https://badge.fury.io/js/%40ngx-translate%2Fcore.svg)](https://badge.fury.io/js/%40ngx-translate%2Fcore)

UNIK is an automated code generation platform for developing quick user interfaces in React and Angular. This platform has two parts a designer and a code generation service. The code repository for the designer is available here [https://code.cognizant.com/111651/unikDesginer]. Refer to the above link for further details.

This is a backend service, UNIK designer interacts with UNIK Code Gen service to generate code. 
Follow the below steps to setup and configure UNIK Code Gen Service. 

## Table of Contents 
* [Pre Requisites](#pre-requisites)
* [Setup](#setup)
* [Usage](#usage)

## Pre Requisites

You can skip the Pre Requisites and move on to [Setup] (#setup), if you had completed this while configuring UNIK designer.
The steps mentioned are specific to a windows based environment. Do follow the equivalent steps for linux or other operating systems.

Ensure you have the following environment variables set properly. Open a command prompt and type the commands given below to ensure it is setup properly and the correct version numbers are displayed.

 Tools        | Recommended Versions | Command 
 ------------ | -------------------- | --------------------------
 Java         | 1.8+                 | java -version
 Maven        | 3.x+                 | mvn --version
 Node         | 10.0.x               | node --version
 Npm          | 5.6.x                | npm --version
 Angular CLI  | 6.0.8                | ng --version
 GIT          | 2.x+                 | git --version
 Python       | 3.x+                 | python --version (This was used by Angular CLI)

Once you have the node installed, install Angular CLI using the below command.

```sh
npm install @angular/cli@6.0.8 -g
```

If you are setting this in Cognizant environment, use this maven [settings.xml](https://code.cognizant.com/111651/unikDesginer/tree/master/src/main/resources/config/settings.xml). Edit the settings.xml file with the path of the local repository, cognizant network credentials and place the file in "%MAVEN_HOME%\conf" folder.

## Setup

1. Navigate to GIT repository [https://code.cognizant.com/238209/ui-code-generator/tree/master]. Download the source code as zip file and extract it to a path in local hard drive.
2. Open command prompt and navigate to the folder in which the source code was extracted.
3. Edit the "package-ui-code-gen.bat" file and update the "resource.path" to "%UNIK_CODE_GEN_EXTRACTED_PATH%\dist". Say if the code is extracted to "C:\UNIK\CodeGen" then the path in the file should be updated to "C:\UNIK\CodeGen\dist".
4. Start the UNIK Code Gen service with the below command.

```sh
cd <UNIK_CODE_GEN_EXTRACTED_PATH>
package-ui-code-gen.bat
```

5. Refer to the UNIK Designer [README] (https://code.cognizant.com/111651/unikDesginer), to access the application. 

## Usage

To be updated