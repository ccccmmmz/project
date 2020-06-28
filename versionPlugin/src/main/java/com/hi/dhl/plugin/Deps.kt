package com.hi.dhl.plugin

import org.gradle.api.Plugin
import org.gradle.api.Project

/**
 * buildSrc
 * config.gradle改变之后需要立即sync now
 *
 * VS
 *
 * Composing builds
 *
 * 依赖的文件改变版本之后不会同步 不知道是不是同时用了2种的缘故
 */
class Deps : Plugin<Project> {
    override fun apply(project: Project) {
    }

    companion object {


    }
}

object Versions {

}