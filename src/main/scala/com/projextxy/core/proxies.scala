package com.projextxy.core

import codechicken.lib.colour.ColourRGBA
import codechicken.lib.packet.PacketCustom
import com.projextxy.core.client.render.block.{RenderCustomGlow, RenderSimpleGlow}
import com.projextxy.core.client.render.item.{RenderXyCustomItemBlock, XychoriumlItemRenderer}
import com.projextxy.core.client.{AnimationFX, RenderTickHandler}
import com.projextxy.core.generator.WorldGeneratorManager
import com.projextxy.core.tile.{TileColorizer, TileXyCustomColor}
import cpw.mods.fml.client.registry.RenderingRegistry
import cpw.mods.fml.common.event.{FMLInitializationEvent, FMLPostInitializationEvent, FMLPreInitializationEvent}
import cpw.mods.fml.common.registry.GameRegistry
import cpw.mods.fml.relauncher.{Side, SideOnly}
import net.minecraft.item.Item
import net.minecraftforge.client.MinecraftForgeClient
import net.minecraftforge.common.MinecraftForge

class CommonProxy {
  def preInit(event: FMLPreInitializationEvent) {
    CoreBlocks.init()
    CoreItems.init()
    GameRegistry.registerTileEntity(classOf[TileXyCustomColor], ProjectXYCore.MOD_ID.toLowerCase + ".tileColorCustomRGB")
    GameRegistry.registerTileEntity(classOf[TileColorizer], ProjectXYCore.MOD_ID.toLowerCase + ".tileColorizer")
  }

  def init(event: FMLInitializationEvent) {
    PacketCustom.assignHandler(ProjectXYCore.MOD_ID, new ServerPacketHandler)
    GameRegistry.registerWorldGenerator(new WorldGeneratorManager, 0)
  }

  def postInit(event: FMLPostInitializationEvent) {
  }
}

class ClientProxy extends CommonProxy {
  var animationFx: AnimationFX = null
  var rainbowColors: Array[ColourRGBA] = Array.fill[ColourRGBA](8)(null)

  @SideOnly(Side.CLIENT)
  override def preInit(event: FMLPreInitializationEvent): Unit = {
    super.preInit(event)
  }

  @SideOnly(Side.CLIENT)
  override def init(event: FMLInitializationEvent): Unit = {
    super.init(event)
    animationFx = new AnimationFX

    RenderingRegistry.registerBlockHandler(new RenderSimpleGlow)
    RenderingRegistry.registerBlockHandler(new RenderCustomGlow)

    MinecraftForgeClient.registerItemRenderer(CoreItems.itemXychorium, new XychoriumlItemRenderer)
    MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(CoreBlocks.blockXyCustom), new RenderXyCustomItemBlock)
    MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(CoreBlocks.blockXyColorizer), new RenderXyCustomItemBlock)
    PacketCustom.assignHandler(ProjectXYCore.MOD_ID, ProjectXyCPH)

    MinecraftForge.EVENT_BUS.register(RenderTickHandler)
  }

  @SideOnly(Side.CLIENT)
  override def postInit(event: FMLPostInitializationEvent) = {
    val freq: Double = math.Pi * 2 / rainbowColors.length
    for (j <- rainbowColors.indices) {
      val red = math.sin(freq * j + 0) * 127 + 128
      val green = math.sin(freq * j + 2) * 127 + 128
      val blue = math.sin(freq * j + 4) * 127 + 128

      rainbowColors(j) = new ColourRGBA(math.floor(red).toInt, math.floor(green).toInt, math.floor(blue).toInt, 255)
    }
    super.postInit(event)
  }
}

object ProjectXYCoreProxy extends ClientProxy
