package de.maxhenkel.voicechat.voice.common;
import net.minecraft.util.math.vector.Vector3d; // Vector3D

public class WorldPosition {
    float x,y,z;
    public WorldPosition(float x, float y, float z){
        this.x = x;
        this.y = y;
        this.z = z;
    }
    public WorldPosition(){
        this.x = 0;
        this.y = 0;
        this.z = 0;
    }
    public void copyFrom(Vector3d vec){
        this.x = (float) vec.x();
        this.y = (float) vec.y();
        this.z = (float) vec.z();
    }
}
