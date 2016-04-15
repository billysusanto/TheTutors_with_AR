package com.thetutors.ar;

import android.content.Context;
import android.view.MotionEvent;

import com.thetutors.R;

import org.rajawali3d.Object3D;
import org.rajawali3d.animation.mesh.SkeletalAnimationObject3D;
import org.rajawali3d.animation.mesh.SkeletalAnimationSequence;
import org.rajawali3d.lights.DirectionalLight;
import org.rajawali3d.loader.Loader3DSMax;
import org.rajawali3d.loader.md5.LoaderMD5Anim;
import org.rajawali3d.loader.md5.LoaderMD5Mesh;
import org.rajawali3d.materials.Material;
import org.rajawali3d.materials.methods.DiffuseMethod;
import org.rajawali3d.materials.textures.Texture;
import org.rajawali3d.math.Quaternion;
import org.rajawali3d.math.vector.Vector3;
import org.rajawali3d.util.RajLog;
import org.rajawali3d.vuforia.RajawaliVuforiaRenderer;

import javax.microedition.khronos.opengles.GL10;

public class AugmentedRealityRenderer extends RajawaliVuforiaRenderer {
	private DirectionalLight mLight;
	private SkeletalAnimationObject3D mBob;
	Object3D lab;
	Object3D tower;

	private AugmentedRealityActivity activity;

    public AugmentedRealityRenderer(Context context) {
		super(context);
		activity = (AugmentedRealityActivity)context;
        //RajLog.setDebugEnabled(false);
	}

	protected void initScene() {
		mLight = new DirectionalLight(.1f, 0, -1.0f);
		mLight.setColor(1.0f, 1.0f, 0.8f);
		mLight.setPower(1);
		
		getCurrentScene().addLight(mLight);
		
		try {
			LoaderMD5Mesh meshParser = new LoaderMD5Mesh(this,
					R.raw.boblampclean_mesh);
			meshParser.parse();
			mBob = (SkeletalAnimationObject3D) meshParser
					.getParsedAnimationObject();
			mBob.setScale(2);

			LoaderMD5Anim animParser = new LoaderMD5Anim("dance", this,
					R.raw.boblampclean_anim);
			animParser.parse();
			mBob.setAnimationSequence((SkeletalAnimationSequence) animParser
					.getParsedAnimationSequence());

			getCurrentScene().addChild(mBob);

			mBob.play();
			mBob.setVisible(false);

			Loader3DSMax objLab = new Loader3DSMax(this, R.raw.labds); // object lab
			objLab.parse();
			lab = objLab.getParsedObject();
			lab.setScale(0.3);
			getCurrentScene().addChild(lab);
			
			Material labMaterial = new Material();
			labMaterial.enableLighting(true);
			labMaterial.setDiffuseMethod(new DiffuseMethod.Lambert());
			labMaterial.addTexture(new Texture("mapdiozu", R.raw.mapdiozu));
			lab.setMaterial(labMaterial);

			Loader3DSMax objTower = new Loader3DSMax(this, R.raw.watertower); // object lab
			objTower.parse();
			tower = objTower.getParsedObject();
			tower.setScale(3);

			getCurrentScene().addChild(tower);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void foundFrameMarker(int markerId, Vector3 position,
		Quaternion orientation) {

		if (markerId == 0) {
			tower.setVisible(true);
			tower.setPosition(position);
			tower.setOrientation(orientation);
		} else if (markerId == 1) {
			lab.setVisible(true);
			lab.setPosition(position);
			lab.setOrientation(orientation);
		}
	}

	@Override
	protected void foundImageMarker(String trackableName, Vector3 position,
			Quaternion orientation) {
		if(trackableName.equals("chips"))
		{
			mBob.setVisible(true);
			mBob.setPosition(position);
			mBob.setOrientation(orientation);
			RajLog.d(activity.getMetadataNative());
		}
		if(trackableName.equals("stones"))
		{
		}
	}

	@Override
	public void noFrameMarkersFound() {
	}


    @Override
	public void onRenderFrame(GL10 glUnused) {
		mBob.setVisible(false);
		//lab.setVisible(false);
		tower.setVisible(false);
		
		super.onRenderFrame(glUnused);
	}

    @Override
    public void onOffsetsChanged(float v, float v2, float v3, float v4, int i, int i2) {

    }

    @Override
    public void onTouchEvent(MotionEvent motionEvent) {

    }

    @Override
    public void onRenderSurfaceSizeChanged(GL10 gl, int width, int height) {
        RajLog.i("boo onRenderSurfaceSizeChanged " + this.hashCode());
        super.onRenderSurfaceSizeChanged(gl, width, height);
    }
}
