# Soul Shards: The Old Ways [![Build Status](http://tehnut.info/jenkins/job/SoulShardsTOW/job/1.10/badge/icon)](http://tehnut.info/jenkins/job/SoulShardsTOW/job/1.10/) [![](http://cf.way2muchnoise.eu/full_soul-shards-the-old-ways_downloads.svg)](https://minecraft.curseforge.com/projects/soul-shards-the-old-ways)

Ever wanted to create your own mob spawners? Now you can!

## Links

* [CurseForge](http://minecraft.curseforge.com/projects/soul-shards-the-old-ways)
* [Jenkins](http://tehnut.info/jenkins/job/SoulShardsTOW/job/1.8.9/)
* [Maven](http://tehnut.info/maven/com/whammich/sstow/SoulShards-TOW/)
* [Questions](https://webchat.esper.net/?channels=tehnut)

## Information

This is a fan continuation of the popular 1.4.7 mod, [Soul Shards](http://www.minecraftforum.net/forums/mapping-and-modding/minecraft-mods/1285901-1-6-4-forgeirc-v1-0-18-soul-shards-v2-0-15-and#soulshards).

This version of the mod is based on the sources of [Soul Shards: Reborn by Moze_Intel](http://www.minecraftforum.net/forums/mapping-and-modding/minecraft-mods/wip-mods/1445947-1-7-10-soul-shards-reborn-original-soul-shards) and [Soul Shards: The Old Ways by Team Whammich](http://www.minecraftforum.net/forums/mapping-and-modding/minecraft-mods/2329877-soul-shards-the-old-ways-rc9-update).

This version is a near direct clone of the original mod.

For more information, you can see the [Wiki](https://github.com/TehNut/Soul-Shards-The-Old-Ways/wiki). Information about modifying the `ShardTiers.json` config can be found there.

##Development Setup

1. Fork this project to your own Github repository and clone it to your desktop.
2. Navigate to the directory you cloned to. Open a command window there and run `gradlew setupDevWorkspace` then (if you use Eclipse) `gradlew eclipse` or (if you use IDEA) `gradlew idea`. 
3. This process will setup [Forge](http://www.minecraftforge.net/forum/), your workspace, and all dependencies.
4. Open the project in your IDE of choice.

####IntelliJ IDEA extra steps

1. Navigate to `File > Settings > Plugins > Browse repositories...`. Search for Lombok and install the plugin.
2. Navigate to `File > Settings > Build, Execution, Deployment > Compiler > Annotation Processors`. Check `Enable annotation processing`. 

[Setup video](https://www.youtube.com/watch?v=8VEdtQLuLO0&feature=youtu.be) by LexManos. For more information, refer to the [Forge Forums](http://www.minecraftforge.net/forum/index.php/topic,14048.0.html).

## Developing Addons

Add to your `build.gradle`:

    repositories {
      maven {
        url "http://tehnut.info/maven/"
      }
    }
    
    dependencies {
      deobfCompile "com.whammic.sstow:SoulShards-TOW:<SSTOW-VERSION>"
    }
`<SSTOW-VERSION>` can be found on CurseForge (or via the Maven itself), check the file name of the version you want.

## License

Soul Shards: The Old Ways for Minecraft 1.8.9 and above is licensed as [MIT](https://tldrlegal.com/license/mit-license).

All of the art is property of [BBoldt](https://github.com/BBoldt/). The art is released into the public domain.

## Custom Builds
   
**Custom builds are *unsupported*. If you have an issue while using an unofficial build, it is not guaranteed that you will get support.**
   
### How to make a custom build:
   
1. Clone directly from this repository to your desktop.
2. Navigate to the directory you cloned to. Open a command window there and run `gradlew build`
3. Once it completes, your new build will be found at `../build/libs/SoulShards-TOW-*.jar`. You can ignore the `api`, `sources`, and `javadoc` jars.