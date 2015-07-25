package com.projextxy.core

import com.projextxy.core.blocks.glow._
import com.projextxy.core.blocks.item.{ItemBLockColorContainer, ItemBlockColor}
import com.projextxy.core.blocks.traits.TConnectedTextureBlock
import com.projextxy.core.client.CTRegistry
import com.projextxy.core.client.render.block.RenderSimpleGlow
import com.projextxy.core.reference.ModColors
import com.projextxy.util.LogHelper
import cpw.mods.fml.common.registry.GameRegistry
import net.minecraft.block.Block
import net.minecraft.block.material.Material
import net.minecraft.item.ItemBlock

object CoreBlocks {
  var blockXyBrick: BlockXyGlow = null
  var blockXyStorage: BlockXyGlow = null
  var blockXyOre: BlockXyOre = null
  var blockXyCustom: BlockXyCustom = null
  var blockXyColorizer: BlockXyColorizer = null
  var blockXyMachineBlock: BlockXyGlow = null


  var blockXyMachineBlockWhite: BlockXyGlow = null

  def init() {
    blockXyOre = new BlockXyOre()

    blockXyStorage = new BlockXyGlow(Material.rock, RenderSimpleGlow.modelId)
    blockXyStorage.setBlockName("blockXyStorage")
    blockXyStorage.colors = ModColors.xyColors

    blockXyBrick = new BlockXyGlow(Material.rock, RenderSimpleGlow.modelId) with ColorMultiplier
    blockXyBrick.setBlockName("blockXyBrick")
    blockXyBrick.colors = ModColors.xyColors

    blockXyMachineBlock = new BlockXyGlow(Material.rock, RenderSimpleGlow.modelId) with TConnectedTextureBlock {
      val connectedFolder = CTRegistry.CLEAN_CONNECTED_FOLDER
    }
    blockXyMachineBlock.setBlockName("blockXyMachineBlock")
    blockXyMachineBlock.colors = ModColors.xyColors

    blockXyMachineBlockWhite = new BlockXyGlow(Material.rock, RenderSimpleGlow.modelId) with TConnectedTextureBlock {
      val connectedFolder = CTRegistry.CLEAN_CONNECTED_FOLDER
    }
    blockXyMachineBlockWhite.setBlockName("blockXyMachineBlockWhite")
    blockXyMachineBlockWhite.colors = ModColors.xyColors

    blockXyCustom = new BlockXyCustom(List(blockXyBrick, blockXyStorage, blockXyMachineBlock, blockXyMachineBlockWhite))
    blockXyColorizer = new BlockXyColorizer()

    registerBlock(blockXyOre, classOf[ItemBlockColor])
    registerBlock(blockXyStorage, classOf[ItemBlockColor])
    registerBlock(blockXyBrick, classOf[ItemBlockColor])
    registerBlock(blockXyMachineBlock, classOf[ItemBlockColor])
    registerBlock(blockXyMachineBlockWhite, classOf[ItemBlockColor])
    registerBlock(blockXyCustom, classOf[ItemBLockColorContainer])
    registerBlock(blockXyColorizer, classOf[ItemBLockColorContainer])
  }

  def registerBlock(block: Block, itemBlock: Class[_ <: ItemBlock]) = {
    if ("tile.null" == block.getUnlocalizedName) throw new RuntimeException(String.format("Block is miising a proper name: %s", block.getClass.getName))
    LogHelper.info("Registering Block: \"%s\" as \"%s\" with item \"%s\"", block.getClass.getSimpleName, block.getUnlocalizedName.substring("tile.".length), itemBlock.getSimpleName)
    GameRegistry.registerBlock(block, itemBlock, block.getUnlocalizedName.substring("tile.".length))
  }

  def registerBlock(block: Block): Block = registerBlock(block, classOf[ItemBlock])
}
