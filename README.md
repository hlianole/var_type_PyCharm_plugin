# var_type_PyCharm_plugin
Plugin for IDE PyChar, that shows variable type at status bar

## Project structure
`/src/main/kotlin/com/hlianole/pycharmplugin`: Project source code

`/build.gradle.kts`: Configuration of gradle build

`/resources/META-INF/plugin.xml`: Plugin definition

## Usage
1.  In IDE:
    `Gradle -> Tasks -> intellij -> runIde`
    This will open PyCharm with this plugin installed

2.  In IDE:
    `Gradle -> Tasks -> intellij -> buildPlugin`
    This will build the plugin as .zip
    Location is `/build/distributions/`
    In PyCharm open Plugin menu (`File -> Settings -> Plugins`)
    In the top right corner click gear icon and choose `Install Plugin from Disk...`
    Choose .zip from `/build/distributions/`