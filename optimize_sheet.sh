#!/bin/bash

# Check if all 2 parameters are provided
if [ "$#" -ne 2 ]; then
    echo "Usage: ./optimize_sheet.sh [input_file] [crop_size]"
    echo "Example: ./optimize_sheet.sh pawn.png 192x192"
    exit 1
fi

INPUT=$1
CROP=$2
OUTPUT=$1

echo "Processing $INPUT..."

# 1. Find the tightest bounding box across all frames
# We use a subshell to trim each frame and find the max dimensions
TIGHT_SIZE=$(magick "assets/graphic/$INPUT" -background none -crop "$CROP" +repage -trim -format "%wx%h\n" info: | sort -r | head -n 1)

if [ -z "$TIGHT_SIZE" ]; then
    echo "Error: Could not calculate tight size. Check your input file and crop dimensions."
    exit 1
fi

echo "Tightest content size found: $TIGHT_SIZE"

# 2. Extract, center, and stitch the frames
TMP="assets/graphic/_tmp_$INPUT"
magick "assets/graphic/$INPUT" -background none -crop "$CROP" +repage \
  -gravity center -extent "$TIGHT_SIZE" +repage \
  +append "$TMP"
mv "$TMP" "assets/graphic/$OUTPUT"

# 3. Output the final dimensions for LibGDX split()
FINAL_W=$(magick identify -format "%w" "assets/graphic/$OUTPUT")
FINAL_H=$(magick identify -format "%h" "assets/graphic/$OUTPUT")
# Count how many frames were created by the initial crop
FRAME_COUNT=$(magick "assets/graphic/$INPUT" -crop "$CROP" -format "%n\n" info: | head -n 1)
TILE_W=$((FINAL_W / FRAME_COUNT))

echo "------------------------------------------------"
echo "Done! Replaced: $OUTPUT"
echo "LibGDX TextureRegion.split() values:"
echo "Width:  $TILE_W"
echo "Height: $FINAL_H"
echo "------------------------------------------------"