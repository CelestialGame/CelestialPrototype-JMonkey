# Celestial

This was a project started by me and [@MitchTalmadge](https://github.com/MitchTalmadge) in 2013 in attempt
to make a [Minecraft](minecraft.net)-[Shores of Hazeron](https://www.hazeron.com/) cross-over game.

With our limited knowledge, this seemed to be a pipe dream. However, we accomplished far more than our expectations.

## Engine

As one might quickly notice, this is completely written in Java. The [jMonkeyEngine](https://jmonkeyengine.org/) was
the engine of choice; we had only designed and developed small Minecraft mods and Java Swing applications prior to
this game's conception.

## Voxel Mesher

Luckily for us, someone with more knowledge than us had developed a voxel framework, [Cubes](https://github.com/jMonkeyEngine-Contributions/cubes).
Thanks to being written so simply, we were able to understand how it worked and build upon it.

## Progress

Well, let the screenshots do the talking:

[TODO: put screenshots]()

## Known Issues and Limitations

Here's what we concluded:
- Floating-point errors make space games hard to do, as [the devs from Kerbal Space Program](https://www.youtube.com/watch?v=mXTxQko-JH0) showed
later on
- Quaterions are hard, but not impossible. [@MitchTalmadge](https://github.com/MitchTalmadge) taught himself enough linear algebra theory to get
our planets to rotate and revolve around the sun.
- Perlin Noise is complicated, but feasbile. I managed to create some sort of random terrain generation using Simplex Noise.
