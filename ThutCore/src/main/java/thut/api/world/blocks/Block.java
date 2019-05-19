package thut.api.world.blocks;

import java.util.UUID;

import javax.annotation.Nullable;

import thut.api.world.Keyed;
import thut.api.world.World;
import thut.api.world.utils.Info;
import thut.api.world.utils.Vector;

public interface Block extends Keyed
{
    /** The world that this block lives in
     * 
     * @return */
    World world();

    void setWorld(World world);

    /** Position of this Block
     * 
     * @return */
    Vector<Integer> position();

    /** Stored info for this block (null if none)
     * 
     * @return */
    @Nullable
    Info info();

    /** UUID of this block (null if no info)
     * 
     * @return */
    @Nullable
    UUID id();
}
