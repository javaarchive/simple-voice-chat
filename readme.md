# Simple Voice Chat ![](http://cf.way2muchnoise.eu/full_416089_downloads.svg) ![](http://cf.way2muchnoise.eu/versions/416089.svg)
## This is a fork intended to be run standalone

## Links
- [CurseForge](https://www.curseforge.com/minecraft/mc-mods/simple-voice-chat)
- [Wiki](https://modrepo.de/minecraft/voicechat/wiki)
- [ModRepo](https://modrepo.de/minecraft/voicechat/overview)
- [GitHub](https://github.com/henkelmax/simple-voice-chat)
- [FAQ](https://modrepo.de/minecraft/voicechat/faq)

---

This mod adds a proximity voice chat to your Minecraft server.
You can choose between push to talk (PTT) or voice activation.
The default PTT key is `CAPS LOCK`, but it can be changed in the controls.
When using voice activation, you can mute your microphone by pressing the `M` key.
You can access the voice chat settings by pressing the `V` key.

## Features

- Proximity voice chat
- Group chats
- [Opus Codec](https://opus-codec.org/)
- Push to talk
- Voice activation
- Configurable PTT key
- Test microphone playback
- Indicator on the screen when you are talking
- Indicator next to players names when they are talking
- Configurable distance
- Mute other players
- Adjust the volume of other players
- Microphone amplification
- Semi 3D sound
- AES encryption
- Configurable voice quality
- Configurable network port

## If you want the rest of the README for the client mod, go to the original mod or look in commit history

## Important Notes

You need to open a port on the server.
This is port `24454/udp` by default.
Without opening this port, the voice chat will not work.
This port can be changed in the server config.
More information [here](https://modrepo.de/minecraft/voicechat/wiki?t=setup).

This mod does only work when connected to a dedicated server.
You need to have this mod installed on the server and the client for it to work.

The Fabric version of this mod allows you to join with vanilla clients,
but you won't be able to use the voice chat features.

The voice chat is encrypted, but I don't guarantee the security of it.
Use at your own risk!
