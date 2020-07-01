# Description
This is a basic setup of [DockerizedCraft](https://github.com/DockerizedCraft/Core) on Docker (without Kubernetes).   
When finishing this quick tutorial you will be able to hack on DockerizedCraft.   
The Dockerizedcraft documentation is here: https://github.com/DockerizedCraft/Core#dockerized-craft---core

# Tutorials
## How to setup DockerizedCraft
1. Install Docker and docker-compose.
    - How to install Docker: https://docs.docker.com/get-docker/
    - How to install docker-compose: https://docs.docker.com/compose/install/
2. Open the terminal/CMD then cd to the directory where you will place Dockerizedcraft
3. Git clone the repository: `git clone https://github.com/bolt-rip/examples.git` then cd to `examples/dockerizedcraft-basic`.   
   Note: If you don't have git installed you can just download the repository from your browser: https://github.com/bolt-rip/examples/archive/master.zip
4. Do the command `docker-compose --project-name minecraft up -d` to launch the full setup.
5. You can now join the lobby through `localhost`.

## How to add your plugin to Bungeecord
1. Edit `docker-compose.yml`
2. Uncomment the 10th line of `docker-compose.yml` and change `/thelocation/whereyourplugin/islocated.jar` to the location of your plugin.  
   Note1: If you are on Windows, just change the location with a location adapted for Windows (C:\yourplugin.jar).   
   Note2: Again on Windows you may encounter an issue with the location, please check this github issue in order to fix the problem: https://github.com/docker/compose/issues/4303#issuecomment-379563170
3. Do the command `docker-compose --project-name minecraft up -d` in order to restart bungeecord.

## How to change the version of the lobbies
1. Edit `docker-compose.yml`
2. Change `VERSION=1.8.8-R0.1-SNAPSHOT-latest` with the version that you want. Example: `VERSION=1.12.2`
3. Do the command `docker-compose --project-name minecraft up -d` in order to restart the lobbies.

## How to see the logs
1. `docker-compose --project-name minecraft logs -f`