package p455w0rd.p455w0rdsthings.client.model;

import javax.annotation.Nonnull;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.inventory.EntityEquipmentSlot;

public class ModelCarbonArmor extends ModelBiped {
	
	ModelRenderer rightshoulder;
	ModelRenderer rightshoulder2;
	ModelRenderer rightArmBand1;
	ModelRenderer rightArmBand2;
	ModelRenderer leftshoulder;
	ModelRenderer leftshoulder2;
	ModelRenderer leftArmBand1;
	ModelRenderer leftArmBand2;
	ModelRenderer leftboot;
	ModelRenderer leftboot2;
	ModelRenderer leftboot3;
	ModelRenderer rightboot;
	ModelRenderer rightboot2;
	ModelRenderer rightboot3;
	ModelRenderer helmet;
	ModelRenderer head;
	ModelRenderer body;
	ModelRenderer rightarm;
	ModelRenderer leftarm;
	ModelRenderer rightleg;
	ModelRenderer leftleg;
	
	private final EntityEquipmentSlot slot;

	public ModelCarbonArmor(EntityEquipmentSlot s) {
		this.slot = s;
		this.textureWidth = 64;
		this.textureHeight = 64;

		this.helmet = new ModelRenderer(this, 0, 41);
		this.helmet.addBox(-4.5F, -6.9F, -4.5F, 9, 1, 9, 0.1F);
		this.helmet.setRotationPoint(0.0F, 0.0F, 0.0F);
		this.helmet.setTextureSize(64, 64);
		this.helmet.mirror = true;
		setRotation(this.helmet, 0.0F, 0.0F, 0.0F);

		this.rightshoulder = new ModelRenderer(this, 0, 34);
		this.rightshoulder.addBox(-4.0F, -3.0F, -2.5F, 5, 1, 5, 0.1F);
		this.rightshoulder.setRotationPoint(0.0F, 0.0F, 0.0F);
		this.rightshoulder.setTextureSize(64, 64);
		this.rightshoulder.mirror = true;
		setRotation(this.rightshoulder, 0.0F, 0.0F, 0.0F);

		this.rightshoulder2 = new ModelRenderer(this, 0, 34);
		this.rightshoulder2.addBox(-4.0F, -1.0F, -2.5F, 5, 1, 5, 0.1F);
		this.rightshoulder2.setRotationPoint(0.0F, 0.0F, 0.0F);
		this.rightshoulder2.setTextureSize(64, 64);
		this.rightshoulder2.mirror = true;
		setRotation(this.rightshoulder2, 0.0F, 0.0F, 0.0F);

		this.rightArmBand1 = new ModelRenderer(this, 0, 34);
		this.rightArmBand1.addBox(-4.0F, 6.0F, -2.5F, 5, 1, 5, 0.1F);
		this.rightArmBand1.setRotationPoint(0.0F, 0.0F, 0.0F);
		this.rightArmBand1.setTextureSize(64, 64);
		this.rightArmBand1.mirror = true;
		setRotation(this.rightArmBand1, 0.0F, 0.0F, 0.0F);

		this.rightArmBand2 = new ModelRenderer(this, 0, 34);
		this.rightArmBand2.addBox(-4.0F, 4.0F, -2.5F, 5, 1, 5, 0.1F);
		this.rightArmBand2.setRotationPoint(0.0F, 0.0F, 0.0F);
		this.rightArmBand2.setTextureSize(64, 64);
		this.rightArmBand2.mirror = true;
		setRotation(this.rightArmBand2, 0.0F, 0.0F, 0.0F);

		this.leftshoulder = new ModelRenderer(this, 0, 34);
		this.leftshoulder.addBox(-1.0F, -3.0F, -2.5F, 5, 1, 5, 0.1F);
		this.leftshoulder.setRotationPoint(0.0F, 0.0F, 0.0F);
		this.leftshoulder.setTextureSize(64, 64);
		this.leftshoulder.mirror = true;
		setRotation(this.leftshoulder, 0.0F, 0.0F, 0.0F);

		this.leftshoulder2 = new ModelRenderer(this, 0, 34);
		this.leftshoulder2.addBox(-1.0F, -1.0F, -2.5F, 5, 1, 5, 0.1F);
		this.leftshoulder2.setRotationPoint(0.0F, 0.0F, 0.0F);
		this.leftshoulder2.setTextureSize(64, 64);
		this.leftshoulder2.mirror = true;
		setRotation(this.leftshoulder2, 0.0F, 0.0F, 0.0F);

		this.leftArmBand1 = new ModelRenderer(this, 0, 34);
		this.leftArmBand1.addBox(-1.5F, 6.0F, -2.5F, 5, 1, 5, 0.1F);
		this.leftArmBand1.setRotationPoint(0.0F, 0.0F, 0.0F);
		this.leftArmBand1.setTextureSize(64, 64);
		this.leftArmBand1.mirror = true;
		setRotation(this.leftArmBand1, 0.0F, 0.0F, 0.0F);

		this.leftArmBand2 = new ModelRenderer(this, 0, 34);
		this.leftArmBand2.addBox(-1.5F, 4.0F, -2.5F, 5, 1, 5, 0.1F);
		this.leftArmBand2.setRotationPoint(0.0F, 0.0F, 0.0F);
		this.leftArmBand2.setTextureSize(64, 64);
		this.leftArmBand2.mirror = true;
		setRotation(this.leftArmBand2, 0.0F, 0.0F, 0.0F);

		this.leftboot = new ModelRenderer(this, 33, 0);
		this.leftboot.addBox(0.0F, 8.0F, -2.5F, 5, 4, 5, 0.1F);
		this.leftboot.setRotationPoint(0.0F, 0.0F, 0.0F);
		this.leftboot.setTextureSize(64, 64);
		this.leftboot.mirror = true;
		setRotation(this.leftboot, 0.0F, 0.0F, 0.0F);

		this.leftboot2 = new ModelRenderer(this, 33, 10);
		this.leftboot2.addBox(0.0F, 6.0F, -2.5F, 5, 1, 5, 0.1F);
		this.leftboot2.setRotationPoint(0.0F, 0.0F, 0.0F);
		this.leftboot2.setTextureSize(64, 64);
		this.leftboot2.mirror = true;
		setRotation(this.leftboot2, 0.0F, 0.0F, 0.0F);

		this.leftboot3 = new ModelRenderer(this, 42, 17);
		this.leftboot3.addBox(0.5F, 7.0F, -2.0F, 4, 1, 4, 0.2F);
		this.leftboot3.setRotationPoint(0.0F, 0.0F, 0.0F);
		this.leftboot3.setTextureSize(64, 64);
		this.leftboot3.mirror = true;
		setRotation(this.leftboot3, 0.0F, 0.0F, 0.0F);

		this.rightboot = new ModelRenderer(this, 33, 0);
		this.rightboot.addBox(-5.0F, 8.0F, -2.5F, 5, 4, 5, 0.1F);
		this.rightboot.setRotationPoint(0.0F, 0.0F, 0.0F);
		this.rightboot.setTextureSize(64, 64);
		this.rightboot.mirror = true;
		setRotation(this.rightboot, 0.0F, 0.0F, 0.0F);

		this.rightboot2 = new ModelRenderer(this, 33, 10);
		this.rightboot2.addBox(-5.0F, 6.0F, -2.5F, 5, 1, 5, 0.1F);
		this.rightboot2.setRotationPoint(0.0F, 0.0F, 0.0F);
		this.rightboot2.setTextureSize(64, 64);
		this.rightboot2.mirror = true;
		setRotation(this.rightboot2, 0.0F, 0.0F, 0.0F);

		this.rightboot3 = new ModelRenderer(this, 42, 17);
		this.rightboot3.addBox(-4.5F, 7.0F, -2.0F, 4, 1, 4, 0.2F);
		this.rightboot3.setRotationPoint(0.0F, 0.0F, 0.0F);
		this.rightboot3.setTextureSize(64, 64);
		this.rightboot3.mirror = false;
		setRotation(this.rightboot3, 0.0F, 0.0F, 0.0F);

		this.head = new ModelRenderer(this, 0, 0);
		this.head.addBox(-4.0F, -8.0F, -4.0F, 8, 8, 8, 0.1F);
		this.head.setRotationPoint(0.0F, 0.0F, 0.0F);
		this.head.setTextureSize(64, 64);
		this.head.mirror = true;
		setRotation(this.head, 0.0F, 0.0F, 0.0F);

		this.body = new ModelRenderer(this, 17, 17);
		this.body.addBox(-4.0F, 0.0F, -2.0F, 8, 12, 4, 0.1F);
		this.body.setRotationPoint(0.0F, 0.0F, 0.0F);
		this.body.setTextureSize(64, 64);
		this.body.mirror = true;
		setRotation(this.body, 0.0F, 0.0F, 0.0F);

		this.rightarm = new ModelRenderer(this, 42, 17);
		this.rightarm.addBox(-3.0F, -2.0F, -2.0F, 4, 12, 4, 0.1F);
		this.rightarm.setRotationPoint(-5.0F, 2.0F, 0.0F);
		this.rightarm.setTextureSize(64, 64);
		this.rightarm.mirror = true;
		setRotation(this.rightarm, 0.0F, 0.0F, 0.0F);

		this.leftarm = new ModelRenderer(this, 42, 17);
		this.leftarm.addBox(-1.0F, -2.0F, -2.0F, 4, 12, 4, 0.1F);
		this.leftarm.setRotationPoint(5.0F, 2.0F, 0.0F);
		this.leftarm.setTextureSize(64, 64);
		this.leftarm.mirror = true;
		setRotation(this.leftarm, 0.0F, 0.0F, 0.0F);

		this.rightleg = new ModelRenderer(this, 0, 17);
		this.rightleg.addBox(-2.0F, 0.0F, -2.0F, 4, 12, 4, 0.1F);
		this.rightleg.setRotationPoint(-2.0F, 12.0F, 0.0F);
		this.rightleg.setTextureSize(64, 64);
		this.rightleg.mirror = true;
		setRotation(this.rightleg, 0.0F, 0.0F, 0.0F);

		this.leftleg = new ModelRenderer(this, 0, 17);
		this.leftleg.addBox(-2.0F, 0.0F, -2.0F, 4, 12, 4, 0.1F);
		this.leftleg.setRotationPoint(2.0F, 12.0F, 0.0F);
		this.leftleg.setTextureSize(64, 64);
		this.leftleg.mirror = true;
		setRotation(this.leftleg, 0.0F, 0.0F, 0.0F);

		this.head.addChild(this.helmet);
		this.leftarm.addChild(this.leftshoulder);
		this.leftarm.addChild(this.leftshoulder2);
		this.leftarm.addChild(this.leftArmBand1);
		this.leftarm.addChild(this.leftArmBand2);
		this.rightarm.addChild(this.rightshoulder);
		this.rightarm.addChild(this.rightshoulder2);
		this.rightarm.addChild(this.rightArmBand1);
		this.rightarm.addChild(this.rightArmBand2);
		this.leftboot.addChild(this.leftboot2);
		this.leftboot.addChild(this.leftboot3);
		this.rightboot.addChild(this.rightboot2);
		this.rightboot.addChild(this.rightboot3);
	}

	public void render(@Nonnull Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks,
			float netHeadYaw, float headPitch, float scale) {
		
		if ((entity instanceof EntityArmorStand)) {
			netHeadYaw = 0.0F;
		}
		this.bipedHeadwear.showModel = false;

		this.bipedHead.showModel = (this.slot == EntityEquipmentSlot.HEAD);
		this.body.showModel = (this.slot == EntityEquipmentSlot.CHEST);
		this.leftarm.showModel = (this.slot == EntityEquipmentSlot.CHEST);
		this.rightarm.showModel = (this.slot == EntityEquipmentSlot.CHEST);
		this.leftleg.showModel = (this.slot == EntityEquipmentSlot.LEGS);
		this.rightleg.showModel = (this.slot == EntityEquipmentSlot.LEGS);
		this.leftboot.showModel = (this.slot == EntityEquipmentSlot.FEET);
		this.rightboot.showModel = (this.slot == EntityEquipmentSlot.FEET);

		this.bipedHead = this.head;
		this.bipedBody = this.body;
		this.bipedRightArm = this.rightarm;
		this.bipedLeftArm = this.leftarm;
		if (this.slot == EntityEquipmentSlot.LEGS) {
			this.bipedRightLeg = this.rightleg;
			this.bipedLeftLeg = this.leftleg;
		}
		else {
			this.bipedRightLeg = this.rightboot;
			this.bipedLeftLeg = this.leftboot;
		}
		super.render(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
	}

	private void setRotation(ModelRenderer model, float x, float y, float z) {
		model.rotateAngleX = x;
		model.rotateAngleY = y;
		model.rotateAngleZ = z;
	}
}
