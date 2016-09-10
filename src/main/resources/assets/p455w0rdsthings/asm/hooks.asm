list n_IItemRenderer
GETSTATIC net/minecraft/client/renderer/tileentity/TileEntityItemStackRenderer.field_147719_a : Lnet/minecraft/client/renderer/tileentity/TileEntityItemStackRenderer;
ALOAD 1
INVOKEVIRTUAL net/minecraft/client/renderer/tileentity/TileEntityItemStackRenderer.func_179022_a (Lnet/minecraft/item/ItemStack;)V
GOTO LEND #end of if statement

list IItemRenderer
ALOAD 2
INSTANCEOF p455w0rd/p455w0rdsthings/lib/render/IItemRenderer
IFEQ LELSE
ALOAD 2
ALOAD 1
INVOKEINTERFACE p455w0rd/p455w0rdsthings/lib/render/IItemRenderer.renderItem (Lnet/minecraft/item/ItemStack;)V
GOTO LEND
LELSE