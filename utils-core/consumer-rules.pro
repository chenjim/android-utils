# Consumer ProGuard rules for utils-core library

# Keep all public utility classes and methods
-keep public class com.chenjim.utils.** {
    public *;
}

# Keep Kotlin metadata
-keepattributes *Annotation*
-keepattributes Signature
-keepattributes InnerClasses
-keepattributes EnclosingMethod

# Keep Kotlin coroutines
-keepnames class kotlinx.coroutines.internal.MainDispatcherFactory {}
-keepnames class kotlinx.coroutines.CoroutineExceptionHandler {}

# Keep utility method names for debugging
-keepattributes SourceFile,LineNumberTable