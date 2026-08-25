<div align="center">

<img src="logo.svg" alt="OakGram" width="140">

# OakGram

A hardened hard fork of Margelet. The previous author clogged the repository with messy code, unable to write proper prompts. We stripped out the junk, rewrote the internals, and built a proper privacy-focused, highly customizable Telegram client.

Defaults: Dark theme, pink accent (#FF4081), zero telemetry, stripped trackers, and anti-leak protections.

[English](README.md) · [Русский](README.ru.md)

</div>

---

### Privacy & Security

- **Run traffic via TOR**: One-tap toggle routing all traffic through your local Tor / Orbot SOCKS5 daemon (`127.0.0.1:9050`).
- **Encrypted DNS (DoH)**: DNS-over-HTTPS via Mullvad, Quad9, or Cloudflare against ISP sniffing and blocking.
- **Duress PIN**: Entering a decoy PIN immediately triggers a complete data destruction (Panic Wipe) and terminates the process.
- **Stealth Accounts**: Hide specific accounts from profile switchers, unlockable only with a secret code in global search.
- **Screenshot Protection (`FLAG_SECURE`)**: Blocks screenshots, screen capture, and hides thumbnails in Recent Apps.
- **Clean Forward**: Forward messages without original author tags or captions.
- **Auto-Cache Cleaning**: Wipes temporary media files and memory caches upon backgrounding.
- **EXIF Stripper**: Strips GPS coords, camera models, and dates from outgoing photos without recompression.
- **Anti-UTM**: Strips tracking tags (`utm_*`, `fbclid`, `si=`, `igsh`, `ref`, etc.) on sending and copying links.
- **Anti-IP Leak**: Blocks direct peer-to-peer in calls — all traffic routes through Telegram relay servers.
- **Ghost Mode**: Suppress "typing..." status, anonymous story viewer, read receipts stealth toggle.
- **Panic Wipe**: One-tap emergency wipe of databases, cache, and session tokens.
- **Local Anti-Delete**: Retains messages and media deleted by other participants on your device.
- **Plugin Firewall**: Permission firewall restricting Python plugins from unauthorized network requests.

---

### Customization

- **Unlimited Pinned Chats**: Pin unlimited chats in folders and dialog list beyond the 5-chat server limit.
- **Code & Monospace Shortcuts**: Quick inline code and code block formatting toggles.
- **Message Bubbles**: Corner radius slider from 0dp (sharp) to 28dp (ultra-round).
- **Avatar Shapes**: Circle, Squircle, or Square.
- **Chat & Controls**: Hide stories bar, hide bot buttons, hide "Send As" selector, disable double-tap reaction, send on Enter.
- **Voice & Media**: Auto-pause music during recording, confirmation before voice dispatch, rear camera for video notes, up to 3.0x playback speed.
- **Folders**: Hide folder labels, show tab counters, hide default "All Chats" tab.

---

### Building

Build using Linux / WSL2 in one click:

```bash
./build.sh
```

Or manually:

```bash
git clone --depth=1 --recursive https://github.com/DrKLO/Telegram.git
cd Telegram
git apply /path/to/OakGram/patch/margelet.patch
cp -r /path/to/OakGram/java/* TMessagesProj/src/main/java/
./gradlew assembleAfatRelease
```

Output APK: `TMessagesProj/build/outputs/apk/afat/release/TMessagesProj-afat-release.apk`
