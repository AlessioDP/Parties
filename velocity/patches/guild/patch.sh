#!/bin/bash
DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" >/dev/null 2>&1 && pwd )"

reg1="s/\/party/\/guild/g"
reg2="s/((:.*[^%/])|(^\s+- '?\"?.*[^%]))(party)([^%])/\1guild\5/g"
reg3="s/((:.*[^%/])|(^\s+- '?\"?.*.*[^%/]))(parties)([^%])/\1guilds\5/g"
reg4="s/((:.*[^%/])|(^\s+- '?\"?.*[^%]))(Party)([^%])/\1Guild\5/g"
reg5="s/Parties List/Guilds List/g"

echo "Patching velocity guild"
cp "$DIR/../../src/main/resources/velocity/messages.yml" "$DIR/messages.yml"
sed -i.original -r "$reg1;$reg2;$reg3;$reg4;$reg5" "$DIR/messages.yml"
echo "Patch completed"