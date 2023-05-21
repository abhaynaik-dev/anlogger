<div align="center">
  <br>
  <h1> 📝  ANLogger 📝 </h1>
  <p>This was a simple module that I used in the majority of my applications; it is now a library. A simple way to retrieve logs from a device without requiring adb access or external read/ write permission, as well as an easy way to send the log file via email. It helps me a lot, hope it does to you too. </p>
  <strong>Happy Debugging !!!</strong>
</div>

#  📖 Prerequisite
1) Use [Timber][1] as a default logging library.
2) Do not plant or use any custom a `Timber` tree, `ANLogger` will take care of everything.

# ⭕ Usage
1) Dependency
```gradle
repositories {
  mavenCentral()
}

dependencies {
  implementation "io.github.abhaynaik-dev:anlogger:0.0.1"
}
```
2) Configure ANLogger 
```kotlin
//Use this in onCreate() of application class
ANLogger.setup(this, "<TAG>" ,ANLoggingLevel.AN_FILE_LOGGING)
//Logging levels
//AN_NONE - To be used in production mode
//AN_FILE_LOGGING - To write logs in file
//AN_ADB_LOGGING - Default Timber.DebugTree() usage, must be used in debug mode only
```
3) Share log file 
> This should be used only with `AN_FILE_LOGGING` logging level\
> If not it will throw runtime `ANLoggerException`
```kotlin
ANLogger.shareLogFile(this)
```
 [1]:https://github.com/JakeWharton/timber
