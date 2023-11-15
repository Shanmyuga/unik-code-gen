@echo off
echo Running maven clean...
call mvn clean

echo(
echo(
echo Running maven install...
call mvn install

echo(
echo(
echo Creating dist folder...
mkdir dist

echo(
echo(
echo Copying UNIK Code Gen Binary...
copy target\fecodegen-1.0.0-SNAPSHOT.jar dist

echo(
echo(
echo Copying UNIK Templates...
xcopy src\main\resources\* dist /S /Y /Q

echo(
echo(
echo Starting UNIK Code Generator...
cd dist
java -Dresource.path=D:\238209\Technical\GitWorkspace\ui-code-generator\dist -jar fecodegen-1.0.0-SNAPSHOT.jar