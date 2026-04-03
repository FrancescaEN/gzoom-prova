ECHO OFF
REM #####################################################################
REM # Licensed to the Apache Software Foundation (ASF) under one
REM # or more contributor license agreements.  See the NOTICE file
REM # distributed with this work for additional information
REM # regarding copyright ownership.  The ASF licenses this file
REM # to you under the Apache License, Version 2.0 (the
REM # "License"); you may not use this file except in compliance
REM # with the License.  You may obtain a copy of the License at
REM # 
REM # http://www.apache.org/licenses/LICENSE-2.0
REM # 
REM # Unless required by applicable law or agreed to in writing,
REM # software distributed under the License is distributed on an
REM # "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
REM # KIND, either express or implied.  See the License for the
REM # specific language governing permissions and limitations
REM # under the License.
REM #####################################################################
@echo off
setlocal ENABLEDELAYEDEXPANSION

if not defined GZOOM_LEGACY_JAVA_VERSION (
	set GZOOM_LEGACY_JAVA_VERSION="11"
)

REM Search for all instances of java.exe
for /f "delims=" %%G in ('where java') do (
    REM Check the java version for each path
	for /f "delims=" %%V in ('""%%G" -version 2>&1 | findstr /r /c:"openjdk version" /c:"java version" "') do (
         REM Parse the version number from the output (e.g., "java version "1.8.0_281"")
         set "version=%%V"
	     set "version=!version:*openjdk version =!"
		 set "version=!version:*java version =!"
         set "version=!version:"=!"
         
         REM Check if this is the version you want (e.g., 1.8.0 for JDK 8)
         echo Found JDK version !version! at %%G
         
         REM Replace 1.8.0 with the version you're looking for
		 echo !version! | findstr /c:"%GZOOM_LEGACY_JAVA_VERSION%"  > result.txt
		 REM Check if findstr found anything
		 for %%A in (result.txt) do (
			if %%~zA gtr 0 (
				echo Selecting JDK version !version!
				set "JDK_PATH=%%~dpG"
				goto :found
			)
		 )
         del result.txt
    )
)

echo No suitable JDK found!
exit /b 1

:found
REM Set JAVA_HOME and update PATH
set JAVA_HOME=%JDK_PATH%
set PATH=%JAVA_HOME%;%JAVA_HOME%\bin;%PATH%

java -jar framework/base/lib/apache-ant-1.10.8/lib/ant-launcher.jar %1 %2 %3 %4 %5 %6
