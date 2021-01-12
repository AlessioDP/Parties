#!/bin/bash
DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" >/dev/null 2>&1 && pwd )"

reg1="s/\/party/\/clan/g"
reg2="s/((:.*[^%/])|(^\s+- '?\"?.*[^%]))(party)([^%])/\1clan\5/g"
reg3="s/((:.*[^%/])|(^\s+- '?\"?.*.*[^%/]))(parties)([^%])/\1clans\5/g"
reg4="s/((:.*[^%/])|(^\s+- '?\"?.*[^%]))(Party)([^%])/\1Clan\5/g"
reg5="s/Parties List/Clans List/g"

echo "Patching bungeecord clan"
cp "$DIR/../../src/main/resources/bungee/messages.yml" "$DIR/messages.yml"
sed -i.original -r "$reg1;$reg2;$reg3;$reg4;$reg5" "$DIR/messages.yml"
echo "Patch completed"