@echo off
SET PATH=%PATH%;C:\TDM-GCC-64\bin;C:\Program Files\Microsoft HPC Pack 2012\Bin
REM SET PATH=%PATH%;C:\TDM-GCC-32\bin;C:\Program Files\Microsoft HPC Pack 2012\Bin
doskey make=mingw32-make.exe $*

cmd

