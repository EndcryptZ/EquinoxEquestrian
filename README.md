# EquinoxEquestrian

> **This plugin is extremely hard-coded for Equinox Server (play.equinoxmc.net). An exclusive plugin.**

A Minecraft plugin for advanced equestrian gameplay and horse management.

---

## 🐴 Overview

**EquinoxEquestrian** introduces a suite of front-end features that provide realistic horse management and immersive equestrian activities for Minecraft servers. Designed for private and advanced use, the plugin centers around comprehensive GUI interactions, player-driven horse management, and stable operations.

---

## ✨ Front-End Features

- **Horse Management Menus:**
  - Intuitive GUIs for selecting, viewing, and managing horses.
  - Inspect horse stats: genetics, breed, coat, traits, health, pregnancy, age, gender, and discipline.
  - Easily claim, rename, release, and transfer horses.

- **Stable & Ownership Controls:**
  - Set privacy or public access for each horse.
  - Assign trusted players for shared management.
  - Cross-tie, lunge, or teleport horses directly from menus.

- **Grooming & Equipment:**
  - Interactive grooming system with dedicated tools.
  - Equip and manage tack, accessories, and horse inventory through item-based GUIs.

- **Training & Lifecycle:**
  - Train horses in specific disciplines and track progress visually.
  - Manage breeding, pregnancy events, and lifecycle changes with menu-driven options.

- **Economy & Token System:**
  - Token-based actions and rewards, integrated into horse management menus.
  - Buy, sell, and trade horses or stable services using built-in economic features.

- **Stable Home & Teleportation:**
  - Set and teleport horses to custom “pasture” or “stall” locations via GUI forms.

- **Bedrock & Java Compatibility:**
  - Unified interface for both Java and Bedrock players.
  - Custom forms and menus adapt for platform-specific interactions.

- **Admin & Player Data Control:**
  - Admin panels for overseeing stables, player horse data, and permissions.
  - Direct menu access for saving/loading player profiles and horse states.

- **Persistent Data:**
  - All menu selections and horse/player states are saved automatically for seamless gameplay and recovery.

---

## 🛠️ Codebase Highlights & Key Modules

- **EquineUtils:**
  - Utility functions for horse identification, stat management, discipline, breeding, lifecycle status, and permission checks.
  - Converts between Minecraft-specific stat formats and realistic values for speed, jump, age, etc.
  - Handles horse slot logic, menu triggers, and applies stat changes.

- **DatabaseUtils:**
  - Modular database schema management for horse/player data.
  - Automatic table updates for schema changes.
  - Maps SQL result sets to in-game horse objects.

- **NBT Keys System:**
  - Enum-based persistent data keys for storing horse attributes: genetics, breed, age, gender, location, privacy, breeding/pregnancy status, hunger, thirst, waste, etc.
  - Methods for reading/writing/checking persistent data on horses.

- **EquineLiveHorse:**
  - Wraps Minecraft AbstractHorse entity with extended stats, breed, traits, and management hooks.

- **EquineBreedingTask & Lifecycle Modules:**
  - Event-driven breeding/pregnancy management.
  - Handles heat cycles, breeding partners, pregnancy events, and instant foal/breed triggers.

- **EquineWasteTask:**
  - Realistic horse waste and hunger mechanics.
  - Periodic checks for horse biological needs, triggering in-game events.

- **Economy/Token Integration:**
  - Core token management for trading, horse actions, and rewards.
  - Economy setup and permissions for server-side control.

- **GUI/Forms:**
  - All gameplay interactions are menu-driven—no command memorization.
  - Adaptive layouts for immersive equestrian simulation.

---

## 👩‍💻 Developer Notes

- All game-changing actions and features are accessed through custom GUIs and forms—no command memorization required.
- The plugin is modular, with managers for horses, menus, player data, and economy systems.
- Menu layouts and options are tailored for immersive, realistic equestrian simulation.

---

> Bring the equestrian world to your Minecraft server with EquinoxEquestrian!