# ThunderStick Plugin

A Minecraft plugin for version 1.21.x that transforms all golden swords into powerful ThunderSticks that summon lightning wherever they strike!

## Features

- **Lightning Strikes**: Every hit with a golden sword summons lightning at the target's location
- **Cooldown System**: Configurable cooldown to prevent spam and maintain balance
- **Particle Effects**: Beautiful electric particle effects for visual flair
- **Sound Effects**: Immersive thunder sounds and cooldown notifications
- **Configurable Damage**: Choose whether lightning deals actual damage or is purely visual
- **Permission System**: Control who can use ThunderSticks
- **Admin Commands**: Easy configuration management

## Installation

1. Download the compiled `.jar` file
2. Place it in your server's `plugins` folder
3. Restart your server
4. Configure the plugin in `plugins/ThunderStick/config.yml`

## Configuration

The plugin comes with a comprehensive configuration file that allows you to customize:

- Cooldown duration
- Lightning damage settings
- Particle effects
- Sound effects
- Chat messages
- Permission requirements

## Commands

- `/thunderstick info` - Display plugin information
- `/thunderstick reload` - Reload the configuration file

## Permissions

- `thunderstick.use` - Allows players to use ThunderStick (default: true)
- `thunderstick.admin` - Allows access to admin commands (default: op)

## Building from Source

1. Clone this repository
2. Make sure you have Maven installed
3. Run `mvn clean package`
4. Find the compiled `.jar` file in the `target` directory

## Compatibility

- Minecraft 1.21.x
- Spigot/Paper servers
- Java 17+

## Support

For issues, suggestions, or contributions, please visit our GitHub repository.

---

**Transform your golden swords into legendary weapons of thunder and lightning!** ⚡