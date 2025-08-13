# Android Utils

一个轻量级的Android工具库集合，提供常用的工具类和扩展函数。专为Android开发者设计，包含日常开发中最常用的工具方法。

[![](https://jitpack.io/v/chenjim/android-utils.svg)](https://jitpack.io/#chenjim/android-utils)
[![API](https://img.shields.io/badge/API-21%2B-brightgreen.svg?style=flat)](https://android-arsenal.com/api?level=21)
[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)

## 功能特性

- ✅ **零依赖**: 不依赖任何第三方库
- ✅ **轻量级**: 代码精简，体积小
- ✅ **线程安全**: 关键操作都考虑了线程安全
- ✅ **易于使用**: API设计简洁明了
- ✅ **高度兼容**: 支持Android API 21+
- ✅ **完整文档**: 详细的使用说明和代码示例

## 模块说明

### utils-core
核心工具模块，包含以下工具类：

#### 📝 基础工具
- `Log` - 增强日志工具，支持文件写入和轮转
- `SafeExecutor` - 安全执行器，自动异常处理和重试机制
- `StringUtils` - 字符串处理工具
- `ValidationUtils` - 数据验证工具

#### 🔢 数据处理
- `NumUtil` - 数字处理和格式化工具
- `TimeUtils` - 时间日期处理工具
- `JsonUtils` - JSON解析和序列化工具
- `CryptoUtils` - 加密解密工具

#### 📱 Android专用
- `DeviceUtils` - 设备信息获取工具
- `UIUtils` - UI相关工具，屏幕适配、视图操作等
- `NetworkUtils` - 网络状态和网络工具
- `FileUtils` - 文件操作工具
- `BitmapUtil` - 位图处理工具
- `PreferenceUtils` - SharedPreferences工具

## 快速开始

### 添加依赖

#### 1. 添加JitPack仓库

在项目根目录的 `build.gradle` 或 `build.gradle.kts` 中添加：

```kotlin
allprojects {
    repositories {
        google()
        mavenCentral()
        maven { url = uri("https://jitpack.io") }
    }
}
```

#### 2. 添加依赖

在模块的 `build.gradle.kts` 中添加：

```kotlin
dependencies {
    implementation("com.github.chenjim:android-utils:1.0.0")
    
    // 还需要添加以下AndroidX依赖（如果项目中没有）
    implementation("androidx.core:core-ktx:1.12.0")
}
```

### 权限要求

某些功能需要添加权限到 `AndroidManifest.xml`：

```xml
<!-- 网络相关功能 -->
<uses-permission android:name="android.permission.INTERNET" />
<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />

<!-- 文件操作功能 -->
<uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
<uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE" />
```

### 基本使用示例

```kotlin
import com.chenjim.utils.*

// 日志使用
Log.d("MyTag", "Debug message")
Log.e("MyTag", "Error message", exception)

// 时间工具
val currentTime = TimeUtils.getCurrentTime()
val timestamp = TimeUtils.getCurrentTimeStamp()

// 数字工具
val safeInt = NumUtil.parseInt("123", 0)
val clampedValue = NumUtil.clamp(150, 0, 100)

// 安全执行
SafeExecutor.safe({
    riskyOperation()
}) { error ->
    Log.e("Error", "Operation failed", error)
}

// 设备信息
val deviceInfo = DeviceUtils.getDeviceInfo(context)
val isTablet = DeviceUtils.isTablet(context)

// UI工具
val screenWidth = UIUtils.getScreenWidth(context)
UIUtils.hideKeyboard(activity)
```

## 详细文档

所有工具类的详细使用方法和API说明都包含在本文档中。您也可以查看源码中的KDoc注释获取更多信息。

## 构建和发布

```bash
# 清理项目
.\gradlew clean

# 编译项目
.\gradlew :utils-core:compileDebugKotlin

# 构建AAR
.\gradlew :utils-core:assembleRelease

# 发布到本地Maven仓库
.\gradlew publishToMavenLocal

# 运行测试
.\gradlew test
```

## 版本历史

- **v1.0.0** (2024-01-01)
  - 初始版本发布
  - 包含14个核心工具类
  - 支持Android API 21+
  - 零第三方依赖

## 贡献指南

欢迎提交Issue和Pull Request！

1. Fork 本仓库
2. 创建特性分支 (`git checkout -b feature/AmazingFeature`)
3. 提交更改 (`git commit -m 'Add some AmazingFeature'`)
4. 推送到分支 (`git push origin feature/AmazingFeature`)
5. 打开Pull Request

## 问题反馈

如果您在使用过程中遇到问题，请通过以下方式反馈：

- [GitHub Issues](https://github.com/chenjim/android-utils/issues)
- 邮箱：me@h89.cn

## 许可证

```
Copyright 2024 Jim Chen

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```