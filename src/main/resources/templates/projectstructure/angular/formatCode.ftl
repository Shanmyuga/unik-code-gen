@echo off

echo Format Typescript and HTML Code...
for /r "src" %%i in (*.component.ts, *.component.html) do ( 
	js-beautify -b none,preserve-inline -Xj -f %%i -o %%i
)