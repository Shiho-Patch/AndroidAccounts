package com.rosan.accounts.ui.theme

import androidx.compose.ui.graphics.Color

/**
 * Material3 Expressive 风格预计算种子色。
 * Expressive 风格使用更鲜艳、对比度更高的主色、次要色、三级色与中性色。
 * 实际 ColorScheme 将通过 dynamicDarkColorScheme / dynamicLightColorScheme
 * (Android 12+) 或自定义 lightColorScheme / darkColorScheme (fallback) 生成。
 */

// Expressive 风格的基础种子色（较 Vibrant，高对比）
val ExpressivePrimarySeed = Color(0xFF6750A4)   // 深紫
val ExpressiveSecondarySeed = Color(0xFF625B71) // 紫灰
val ExpressiveTertiarySeed = Color(0xFF7D5260)  // 玫瑰
val ExpressiveNeutralSeed = Color(0xFF1C1B1F)   // 深中性
val ExpressiveNeutralVariantSeed = Color(0xFF49454F) // 中性变体

// Expressive 预调色板 (fallback 静态色彩方案使用)
// Light Expressive
val ExpressiveLightPrimary = Color(0xFF6750A4)
val ExpressiveLightOnPrimary = Color(0xFFFFFFFF)
val ExpressiveLightPrimaryContainer = Color(0xFFEADDFF)
val ExpressiveLightOnPrimaryContainer = Color(0xFF21005D)

val ExpressiveLightSecondary = Color(0xFF625B71)
val ExpressiveLightOnSecondary = Color(0xFFFFFFFF)
val ExpressiveLightSecondaryContainer = Color(0xFFE8DEF8)
val ExpressiveLightOnSecondaryContainer = Color(0xFF1D192B)

val ExpressiveLightTertiary = Color(0xFF7D5260)
val ExpressiveLightOnTertiary = Color(0xFFFFFFFF)
val ExpressiveLightTertiaryContainer = Color(0xFFFFD8E4)
val ExpressiveLightOnTertiaryContainer = Color(0xFF31111D)

val ExpressiveLightBackground = Color(0xFFFFFBFE)
val ExpressiveLightOnBackground = Color(0xFF1C1B1F)
val ExpressiveLightSurface = Color(0xFFFFFBFE)
val ExpressiveLightOnSurface = Color(0xFF1C1B1F)
val ExpressiveLightSurfaceVariant = Color(0xFFE7E0EC)
val ExpressiveLightOnSurfaceVariant = Color(0xFF49454F)

val ExpressiveLightOutline = Color(0xFF79747E)
val ExpressiveLightOutlineVariant = Color(0xFFCAC4D0)
val ExpressiveLightError = Color(0xFFB3261E)
val ExpressiveLightOnError = Color(0xFFFFFFFF)

val ExpressiveLightSurfaceContainerLowest = Color(0xFFFFFFFF)
val ExpressiveLightSurfaceContainerLow = Color(0xFFF7F2FA)
val ExpressiveLightSurfaceContainer = Color(0xFFF3EDF7)
val ExpressiveLightSurfaceContainerHigh = Color(0xFFECE6F0)
val ExpressiveLightSurfaceContainerHighest = Color(0xFFE6E0E9)

// Dark Expressive
val ExpressiveDarkPrimary = Color(0xFFD0BCFF)
val ExpressiveDarkOnPrimary = Color(0xFF381E72)
val ExpressiveDarkPrimaryContainer = Color(0xFF4F378B)
val ExpressiveDarkOnPrimaryContainer = Color(0xFFEADDFF)

val ExpressiveDarkSecondary = Color(0xFFCCC2DC)
val ExpressiveDarkOnSecondary = Color(0xFF332D41)
val ExpressiveDarkSecondaryContainer = Color(0xFF4A4458)
val ExpressiveDarkOnSecondaryContainer = Color(0xFFE8DEF8)

val ExpressiveDarkTertiary = Color(0xFFEFB8C8)
val ExpressiveDarkOnTertiary = Color(0xFF492532)
val ExpressiveDarkTertiaryContainer = Color(0xFF633B48)
val ExpressiveDarkOnTertiaryContainer = Color(0xFFFFD8E4)

val ExpressiveDarkBackground = Color(0xFF1C1B1F)
val ExpressiveDarkOnBackground = Color(0xFFE6E0E9)
val ExpressiveDarkSurface = Color(0xFF1C1B1F)
val ExpressiveDarkOnSurface = Color(0xFFE6E0E9)
val ExpressiveDarkSurfaceVariant = Color(0xFF49454F)
val ExpressiveDarkOnSurfaceVariant = Color(0xFFCAC4D0)

val ExpressiveDarkOutline = Color(0xFF938F99)
val ExpressiveDarkOutlineVariant = Color(0xFF49454F)
val ExpressiveDarkError = Color(0xFFF2B8B5)
val ExpressiveDarkOnError = Color(0xFF601410)

val ExpressiveDarkSurfaceContainerLowest = Color(0xFF121114)
val ExpressiveDarkSurfaceContainerLow = Color(0xFF1C1B1F)
val ExpressiveDarkSurfaceContainer = Color(0xFF211F26)
val ExpressiveDarkSurfaceContainerHigh = Color(0xFF2B2930)
val ExpressiveDarkSurfaceContainerHighest = Color(0xFF36343B)
