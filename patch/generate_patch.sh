#!/bin/bash
cd /home/rorka/Проекты/libregram/Telegram
git diff > /home/rorka/Проекты/Margelet/patch/margelet.patch
echo "Updated margelet.patch ($(wc -l < /home/rorka/Проекты/Margelet/patch/margelet.patch) lines)"
