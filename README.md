# EquinoxEquestrian

A Minecraft plugin for advanced equestrian gameplay and horse management.

---

## 🐴 Overview

**EquinoxEquestrian** is a feature-rich Minecraft plugin that brings realistic horse management and equestrian activities to your server. It provides in-depth mechanics for horse handling, breeding, grooming, training, and stable management, aiming to enhance the equestrian experience for players.

---

## ✨ Features

- **Advanced Horse Management:**  
  - Breed, train, and manage horses with unique genetics, coats, traits, and stats.
  - Track horse age, gender, prominent and mixed breeds, discipline, and traits.
  - Manage horse health, pregnancy, and other lifecycle events.

- **Stable & Ownership System:**  
  - Claim, cross-tie, lunge, and teleport horses.
  - Privacy and public settings for horses.
  - Permissions system to control player interactions with horses.

- **Grooming & Equipment:**  
  - Use dedicated grooming tools and items.
  - Manage equipment and inventory related to horses.

- **GUI Integration:**  
  - Menus for horse management and building (via SpiGUI and custom forms).
  - Bedrock/Java compatibility features.

- **Economy & Tokens:**  
  - Token-based systems for in-game rewards and actions.
  - Economy integration for buying and selling horses or services.

- **PlaceholderAPI Support:**  
  - Integration with PlaceholderAPI for dynamic server placeholders.

- **Database Persistence:**  
  - All player and horse data is stored in a persistent SQLite database.

---

## 🛠️ Installation

1. **Download** the latest release from the [releases page](https://github.com/EndcryptZ/EquinoxEquestrian/releases).
2. **Drag and drop** the `.jar` file into your server's `plugins/` directory.
3. **Restart** your Minecraft server.
4. **(Optional)** Configure plugin settings in the generated config files.

---

## 🏇 Usage

- Use in-game commands and GUIs to interact with horse features (see `/help equinox` for details).
- Manage horses via menus, claim and train them, and use tokens to unlock special features.
- Admins can manage player data and oversee stable operations.

---

## 🔌 Dependencies

- [ProtocolLib](https://www.spigotmc.org/resources/protocollib.1997/)
- [PlaceholderAPI](https://www.spigotmc.org/resources/placeholderapi.6245/) (optional, for placeholders)
- [Floodgate](https://github.com/GeyserMC/Floodgate) (for Bedrock support)
- [SpiGUI](https://github.com/SpiGetOrg/SpiGUI)
- [NBT-API](https://github.com/tr7zw/Item-NBT-API)
- [Vault](https://www.spigotmc.org/resources/vault.34315/) (for economy features)

---

## 👩‍💻 Development

- **Language:** Java
- **API:** Spigot/Bukkit

The main plugin class is `EquinoxEquestrian`, and the code is modularized into managers for horses, databases, commands, and menus. See the `src/main/java/endcrypt/equinox/` directory for the code structure.

---

## 🤝 Contributing

Contributions are welcome! Please open issues or pull requests for suggestions, bugfixes, or new features.

---

## 📄 License

MIT License. See [`LICENSE`](LICENSE) for details.

---

## 📬 Contact

- **Author:** [EndcryptZ](https://github.com/EndcryptZ)
- **Issues:** [GitHub Issues](https://github.com/EndcryptZ/EquinoxEquestrian/issues)
- **Discord:** _Coming soon_

---

> Bring the equestrian world to your Minecraft server with EquinoxEquestrian!
