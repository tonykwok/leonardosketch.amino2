package com.joshondesign.amino.tests;

import com.joshondesign.amino.*;
import com.joshondesign.amino.nodes.GroupNode;
import com.joshondesign.amino.nodes.Node;
import com.joshondesign.amino.nodes.NodeCreator;
import com.joshondesign.amino.nodes.ShapeNode;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

/**
 * This is a demo to test the speed of my stack.
 * It creates N particles which start in the center,
 * and move based on velocity, gravity, friction.
 * they are drawn as a small shared texture which is ADD blended
 * to the screen. the screen is cleared between each frame.
 *
 *
 *
 *
 */
public class ParticlesDemo implements NodeCreator {
    private static final int MAX_PARTICLES = 3000;
    private static final int NEW_PARTICLES_PER_TICK = 10;


    private GroupNode group;
    private List<Particle> particles;
    private static final int SCREEN_WIDTH = 600;
    private static final int SCREEN_HEIGHT = 400;

    public static void main(String ... args) throws Exception {
        Core.init("jogl");
        Core.getImpl().init(new ParticlesDemo());
    }

    public Node create() throws NoSuchMethodException {
        particles = new ArrayList<Particle>();
        group = new GroupNode();
        Core.getImpl().add(new Animateable() {
            public void process(long startTime, long currentTime) throws InvocationTargetException, IllegalAccessException {
                update();
            }
        });
        return group;
    }

    private void update() {
        if(particles.size() < MAX_PARTICLES) {
            for(int i=0; i<NEW_PARTICLES_PER_TICK; i++) {
                Particle p = new Particle();
                p.x = SCREEN_WIDTH / 2 + random(-10, 10);
                p.y = SCREEN_HEIGHT / 2 + random(-10, 10);
                p.xv = random(-1,1);
                p.yv = random(-1,1);
                p.setFill(Color.rgb(0.2,1.0,0.8));
                particles.add(p);
                group.add(p);
            }
        }
        for(Particle n : particles) {
            n.x += n.xv;
            n.y += n.yv;

            if(n.x < -100 || n.x > SCREEN_WIDTH + 100) {
                n.x = SCREEN_WIDTH/2;
                n.y = SCREEN_HEIGHT / 2;
            }
            if(n.y < -100 || n.y > SCREEN_HEIGHT + 100) {
                n.x = SCREEN_WIDTH/2;
                n.y = SCREEN_HEIGHT/2;
            }
        }

    }

    private double random(double min, double max) {
        return min + Math.random()*(max-min);
    }

    private void p(String updating) {
        System.out.println(updating);
    }

    class Particle extends ShapeNode {
        double xv;
        double yv;
        public double x;
        public double y;

        @Override
        public void draw(Gfx gfx) {
            gfx.setFill(Color.rgba(0.5,0.5,0.5,0.5));
            Path path = Path.moveTo(x,y)
                    .lineTo(x+30,y)
                    .lineTo(x+40,y+20)
                    .lineTo(x+10,y+30)
                    .lineTo(x, y+10)
                    .closeTo().build();
            gfx.fill(path);
        }
    }
}
