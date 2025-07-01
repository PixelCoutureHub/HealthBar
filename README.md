# LifeLineX

**Advanced Health Bar Display Plugin for Minecraft Java Edition**

LifeLineX is a comprehensive health bar display plugin that brings the popular Bedrock Edition health bar resource pack functionality to Minecraft Java Edition servers. Created by Pixeldev, this plugin provides real-time health visualization for all entities with extensive customization options.

## Features

### 🎯 Core Features
- **Real-time Health Bars**: Display health bars above all living entities
- **Multiple Display Modes**: Always show, hide until hurt, or damage-only display
- **Entity Support**: Full support for players, mobs, and bosses
- **Custom Resource Pack**: Automatically generated resource pack with custom textures
- **Distance-based Display**: Configurable maximum display distance
- **World-specific Settings**: Enable/disable per world

### 🎨 Visual Features
- **Custom Textures**: High-quality health bar and heart textures
- **Dynamic Colors**: Health bars change color based on health percentage
- **Smooth Animations**: Fluid health bar updates and transitions
- **Proper Scaling**: Health bars scale appropriately for different entity sizes
- **Position Optimization**: Smart positioning above entities

### ⚙️ Configuration Options
- **Display Modes**:
  - `ALWAYS_SHOW`: Health bars are always visible
  - `HIDE_UNTIL_HURT`: Only show when entity is damaged
  - `DAMAGE_ONLY`: Show only during damage events
- **Entity Filtering**: Choose which entity types to display
- **Performance Settings**: Configurable update intervals
- **Distance Control**: Set maximum display distance
- **World Management**: Disable in specific worlds

## Installation

1. Download the latest release from the [releases page](https://github.com/pixeldev/LifeLineX/releases)
2. Place the `LifeLineX.jar` file in your server's `plugins` folder
3. Restart your server
4. The plugin will automatically generate configuration files and resource pack

## Commands

| Command | Description | Permission |
|---------|-------------|------------|
| `/lifelinex help` | Show help message | `lifelinex.use` |
| `/lifelinex info` | Show plugin information | `lifelinex.use` |
| `/lifelinex reload` | Reload configuration | `lifelinex.admin` |
| `/lifelinex toggle` | Toggle health bars on/off | `lifelinex.admin` |
| `/lifelinex mode <mode>` | Set display mode | `lifelinex.admin` |
| `/lifelinex distance <number>` | Set max display distance | `lifelinex.admin` |
| `/lifelinex resourcepack` | Apply resource pack | `lifelinex.use` |

**Aliases**: `llx`, `healthbar`, `hb`

## Permissions

| Permission | Description | Default |
|------------|-------------|---------|
| `lifelinex.*` | All permissions | OP |
| `lifelinex.admin` | Administrative permissions | OP |
| `lifelinex.use` | Basic usage permissions | True |
| `lifelinex.reload` | Reload configuration | OP |
| `lifelinex.toggle` | Toggle health bars | OP |
| `lifelinex.config` | Modify configuration | OP |

## Configuration

The plugin creates a comprehensive configuration file with the following options:

```yaml
# Enable or disable the health bar display
enabled: true

# Display mode: ALWAYS_SHOW, HIDE_UNTIL_HURT, DAMAGE_ONLY
display-mode: HIDE_UNTIL_HURT

# Entity type settings
show-for-players: true
show-for-mobs: true
show-for-bosses: true

# Performance settings
max-distance: 32.0
update-interval: 5

# Resource pack settings
use-custom-textures: true

# Disabled worlds
disabled-worlds:
  - "example_world"
```

## Supported Entities

LifeLineX supports health bars for all living entities, including:

### Players & NPCs
- Players
- Villagers
- Wandering Traders

### Passive Mobs
- Cows, Pigs, Sheep, Chickens
- Horses, Donkeys, Mules, Llamas
- Cats, Dogs, Parrots, Rabbits
- And many more...

### Hostile Mobs
- Zombies, Skeletons, Creepers
- Endermen, Spiders, Witches
- Pillagers, Vindicators, Evokers
- All Nether mobs

### Bosses
- Ender Dragon
- Wither
- Elder Guardian
- Warden

## Performance

LifeLineX is designed with performance in mind:
- **Efficient Updates**: Configurable update intervals to balance performance and responsiveness
- **Distance Culling**: Only process entities within configured distance
- **Smart Caching**: Intelligent caching system to reduce computational overhead
- **Async Processing**: Non-blocking operations where possible

## Compatibility

- **Minecraft Version**: 1.21.6+
- **Server Software**: Paper, Spigot, Bukkit
- **Java Version**: 21+
- **Dependencies**: None required

## Resource Pack

LifeLineX automatically generates a custom resource pack that includes:
- High-quality health bar textures (51 different states)
- Custom heart icon
- Number font textures (0-9)
- Optimized for performance and visual quality

The resource pack is automatically applied to players when they join (if enabled in config).

## API

LifeLineX provides a simple API for developers:

```java
// Get the plugin instance
LifeLineX plugin = LifeLineX.getInstance();

// Access the health bar manager
HealthBarManager manager = plugin.getHealthBarManager();

// Trigger health bar update for an entity
manager.onEntityDamage(entity);

// Remove health bar for an entity
manager.removeHealthBar(entity.getUniqueId());
```

## Building from Source

1. Clone the repository:
   ```bash
   git clone https://github.com/pixeldev/LifeLineX.git
   ```

2. Navigate to the project directory:
   ```bash
   cd LifeLineX
   ```

3. Build with Maven:
   ```bash
   mvn clean package
   ```

4. The compiled JAR will be in the `target` directory

## Contributing

Contributions are welcome! Please feel free to submit a Pull Request. For major changes, please open an issue first to discuss what you would like to change.

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## Support

If you encounter any issues or have questions:
- Open an issue on [GitHub](https://github.com/pixeldev/LifeLineX/issues)
- Join our [Discord server](https://discord.gg/pixeldev)
- Check the [Wiki](https://github.com/pixeldev/LifeLineX/wiki) for detailed documentation

## Credits

- **Original Concept**: Based on the Bedrock Edition Health Bar resource pack by SMDGAMERZ
- **Developer**: Pixeldev
- **Special Thanks**: The Minecraft modding community for inspiration and support

---

**Made with ❤️ by Pixeldev**