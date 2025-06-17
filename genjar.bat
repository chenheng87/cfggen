chcp 65001
call gradle -PnoPoi shadowJar
copy /B /Y app\build\libs\configgen.jar cfggen.jar
pause