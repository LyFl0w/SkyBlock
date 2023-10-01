# SkyBlock

The QuillCraft SkyBlock Plugin offers an exciting twist on the classic SkyBlock game mode by transporting players to a futuristic space-themed world. Prepare to embark on an intergalactic adventure in the skies, build your floating islands, and conquer the challenges that await.

## Overview

- **Futuristic SkyBlock:** QuillCraft's SkyBlock takes place in a dazzling, futuristic space environment. Explore a world filled with advanced technology, stunning visuals, and futuristic challenges.

- **Endless Creativity:** With the QuillCraft SkyBlock Plugin, you have the freedom to build your own floating islands, create intricate structures, and design your space base with advanced technology.

- **Challenging Missions:** Challenge yourself with a variety of space-themed missions, quests, and objectives that test your skills and resourcefulness.

- **Community and Economy:** Build a thriving community within the futuristic space world and participate in a player-driven economy.

## Clone this project!! 🛑

To clone this fork of [LyFl0w's SkyBlock](https://github.com/LyFl0w/SkyBlock.git), follow these steps:

1. Clone the QuillCraft SkyBlock plugin repository using the following command:
```shell
git clone https://github.com/QuillCraftMC/SkyBlock.git
```

2. Add the option to fetch updates from the original SkyBlock repository using this command:
```shell
git remote add upstream https://github.com/LyFl0w/SkyBlock.git
git remote set-url --push upstream DISABLE
```

3. When pushing changes to the repository, use the following command to push to the origin:
```shell
git push origin
```

4. To pull changes from the upstream original SkyBlock repository and rebase on top of your work, use the following commands:
```shell
git fetch upstream
git rebase upstream/master
```

## Compile
Use maven to compile the SkyBlock Plugin
```shell
mvn clean install
```
