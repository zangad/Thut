package thut.api.world.mobs;

import java.util.UUID;

import thut.api.world.Keyed;
import thut.api.world.World;
import thut.api.world.mobs.ai.AI;
import thut.api.world.utils.Info;
import thut.api.world.utils.Vector;

/** This is a Mob, it is located at a point, which is a Vector, and lives in a
 * World, it can move, with a velocity (which is also a Vector).<br>
 * <br>
 * In other words, this mob lives on the tangent bundle of the configuration
 * manifold represented by the World.
 * 
 * @author Thutmose */
public interface Mob extends Keyed
{
    /** The world this mob lives in.
     * 
     * @return */
    World world();

    void setWorld(World world);

    /** Where this mob is on the world grid.
     * 
     * @return */
    Vector<Integer> worldPos();

    /** Where the mob is
     * 
     * @return */
    Vector<Double> position();

    /** How fast this mob is moving
     * 
     * @return */
    Vector<Double> velocity();

    /** This is the client side version of the mob.
     * 
     * @return */
    boolean onClient();

    /** @return Whether is actually added to a world. */
    boolean inWorld();

    /** If true, this mob is dead, and will be cleaned up from the world.
     * 
     * @return */
    boolean isDead();

    /** Sets this mob as dead. */
    void setDead();

    /** Stored info for this mob.
     * 
     * @return */
    Info info();

    /** The AI object for this mob.
     * 
     * @return */
    AI getAI();

    /** UUID of this mob.
     * 
     * @return */
    UUID id();

    void setID(UUID id);

    /** This is whatever is being wrapped by this Mob, it is usually something
     * like an Entity.
     * 
     * @return */
    Object wrapped();

    /** Maximum health this mob can have.
     * 
     * @return */
    float getMaxHealth();

    /** Sets the current health for this mob.
     * 
     * @param health */
    void setHealth(float health);
}
