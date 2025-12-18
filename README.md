# CLI Metronome (Java)

A **Java command-line metronome** that plays an audible click (“doot”) at a chosen tempo. Supports **manual BPM entry** or **tap tempo** input, then runs until you press Enter to stop.

## Overview

This project is a lightweight CLI tool meant to practice:
- basic Java program structure + console I/O
- timing with `Thread.sleep()`
- playing a short audio sample using the Java Sound API

On launch, you can either:
- enter a BPM number (e.g. `120`), or
- type `tap` and tap Enter 3 times to estimate BPM

## Features

- **Manual BPM mode**
  - Enter an integer BPM and start the metronome
- **Tap tempo mode**
  - Tap Enter **3 times**
  - Automatically calculates BPM from the average interval
  - If you wait **> 2 seconds** between taps, it resets and asks you to start over
- **Audio click + console output**
  - Prints `Doot` each beat and plays `doot.wav`
- **Press Enter to stop** the metronome cleanly

## Project Structure

- `src/Main.java` — CLI + metronome/tap-tempo logic
- `src/doot.wav` — click sound played on each beat (loaded as a resource)

## How It Works

- Beat interval is computed as:

  `delay_ms = 60000 / bpm`

- The metronome runs on a separate thread that:
  - prints `Doot`
  - calls `playDootSound()`
  - sleeps for `delay_ms`
- The main thread blocks on `scanner.nextLine()` and interrupts the metronome thread when you press Enter.

## Requirements

- Java (JDK 8+ recommended)
- Audio playback support via `javax.sound.sampled`

## Running

### Compile + Run (Terminal)

From the project root (adjust paths if needed):

```bash
javac -d out src/Main.java
java -cp out Main
