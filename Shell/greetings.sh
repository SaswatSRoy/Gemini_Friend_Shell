#!/bin/bash


if ! command -v yad &> /dev/null; then
    echo "Error: 'yad' is not installed. Please install it." >&2
    exit 1
fi
if ! command -v curl &> /dev/null; then
    echo "Error: 'curl' is not installed. Please install it." >&2
    exit 1
fi

# --- 2. Dynamic Variables ---
HOUR=$(date +%H)
FULL_NAME=$(getent passwd "$USER" | cut -d: -f5 | cut -d, -f1)
NAME=${FULL_NAME:-$USER}
FULL_DATE=$(date +"%A, %d %B %Y")


LOCATION="Rourkela,IN"
WEATHER_DATA=$(curl --connect-timeout 5 -s "wttr.in/$LOCATION?format=%C:%t:%w")

if [ $? -eq 0 ] && [ -n "$WEATHER_DATA" ]; then
    IFS=':' read -r CONDITION TEMP WIND <<< "$WEATHER_DATA"
    WEATHER_INFO="Weather in $LOCATION: $CONDITION, $TEMP, Wind: $WIND"
else
    WEATHER_INFO="Could not retrieve live weather data."
fi

UPTIME_INFO="System has been $(uptime -p)."


case $HOUR in
    05|06|07|08|09|10|11) # Morning
        GREETING="Good Morning, $NAME"
        BG_COLOR="#E0F7FA"
        ICON="weather-sunny" # More common than 'weather-clear'
        ACK_BUTTON_TEXT="Have a productive day!"
        ;;
    12|13|14|15|16) # Afternoon
        GREETING="Good Afternoon, $NAME"
        BG_COLOR="#FFF9C4"
        ICON="weather-overcast" # More common than 'weather-few-clouds'
        ACK_BUTTON_TEXT="Keep up the great work!"
        ;;
    17|18|19|20|21) # Evening
        GREETING="Good Evening, $NAME"
        BG_COLOR="#D1C4E9"
        ICON="weather-haze" # Good standard icon for sunset/evening
        ACK_BUTTON_TEXT="Time to unwind!"
        ;;
    *) # Night
        GREETING="Good Night, $NAME"
        BG_COLOR="#37474F"
        TEXT_COLOR="#ECEFF1"
        ICON="weather-night" # More common than 'weather-clear-night'
        ACK_BUTTON_TEXT="Sweet Dreams!"
        ;;
esac

# --- 5. Construct the Display Message ---
MAIN_TEXT="<span size='xx-large' font_weight='bold' foreground='${TEXT_COLOR:-#000000}'>$GREETING</span>"
DATE_TEXT="<span size='large' foreground='${TEXT_COLOR:-#212121}'>Today is $FULL_DATE.</span>"
INFO_TEXT="<span size='medium' font_style='italic' foreground='${TEXT_COLOR:-#333333}'>$WEATHER_INFO\n$UPTIME_INFO</span>"
YAD_TEXT="$MAIN_TEXT\n\n$DATE_TEXT\n\n$INFO_TEXT"

# --- 6. Display the 'yad' Dialog Box ---
yad --title="System Greeting" \
    --window-icon="info" \
    --image="$ICON" \
    --image-on-top \
    --width=600 \
    --height=250 \
    --text-align=center \
    --text="$YAD_TEXT" \
    --button="$ACK_BUTTON_TEXT:0" \
    --button="Check for Updates:2" \
    --timeout=20 \
    --no-escape \
    --on-top \
    --center \
    --background="$BG_COLOR"

# --- 7. Handle Interactive Button Clicks ---
exit_code=$?

if [ $exit_code -eq 2 ]; then
    # BONUS: This command now waits for you to press Enter before closing the terminal.
    x-terminal-emulator -e "bash -c 'echo \"Checking for system updates...\"; sudo apt update && sudo apt full-upgrade -y; echo; read -p \"Press Enter to close window...\"'"
fi

exit 0
