package com.aoh4.frontline;

import java.util.*;

/**
 * Independent planning engine. Requires an adapter to the actual AoH4 engine.
 * No game files are patched by this class alone.
 */
public final class FrontlineSystem {
    public enum Order { HOLD, DEFEND, ATTACK, STOP }

    public interface World {
        int ownerOf(int province);
        Collection<Integer> neighborsOf(int province);
        Collection<Integer> provincesOf(int civilization);
        int locationOf(long armyId);
        int armyStrength(long armyId);
        int distance(int fromProvince, int toProvince);
        void moveArmy(long armyId, int province);
        void attackProvince(long armyId, int province);
        boolean isAtWar(int civilizationA, int civilizationB);
    }

    public static final class Front {
        public final String id;
        public final int owner;
        public final int enemy;
        public final String name;
        public final int colorRgb;
        public Order order = Order.HOLD;
        public final Set<Long> armies = new LinkedHashSet<>();
        public final Set<Integer> selectedBorderProvinces = new LinkedHashSet<>();

        private Front(String id, int owner, int enemy, String name, int colorRgb) {
            this.id = id;
            this.owner = owner;
            this.enemy = enemy;
            this.name = name;
            this.colorRgb = colorRgb;
        }
    }

    private final Map<String, Front> fronts = new LinkedHashMap<>();

    public Front create(int owner, int enemy, String name, int colorRgb) {
        if (owner == enemy) throw new IllegalArgumentException("Enemy must differ");
        Front front = new Front(UUID.randomUUID().toString(), owner, enemy, name, colorRgb);
        fronts.put(front.id, front);
        return front;
    }

    public Collection<Front> fronts() {
        return Collections.unmodifiableCollection(fronts.values());
    }

    public boolean remove(String id) { return fronts.remove(id) != null; }

    public void assign(String frontId, long armyId) {
        Front target = required(frontId);
        for (Front front : fronts.values()) front.armies.remove(armyId);
        target.armies.add(armyId);
    }

    public void setOrder(String frontId, Order order) {
        required(frontId).order = Objects.requireNonNull(order);
    }

    public void selectBorder(String frontId, Collection<Integer> provinces) {
        Front front = required(frontId);
        front.selectedBorderProvinces.clear();
        front.selectedBorderProvinces.addAll(provinces);
    }

    public List<Integer> border(World world, Front front) {
        List<Integer> result = new ArrayList<>();
        for (int province : world.provincesOf(front.owner)) {
            if (!front.selectedBorderProvinces.isEmpty()
                    && !front.selectedBorderProvinces.contains(province)) continue;
            for (int adjacent : world.neighborsOf(province)) {
                if (world.ownerOf(adjacent) == front.enemy) {
                    result.add(province);
                    break;
                }
            }
        }
        Collections.sort(result);
        return result;
    }

    /** Call at most once per game turn. Never call each render frame. */
    public void tick(World world) {
        for (Front front : fronts.values()) {
            if (front.order == Order.STOP) continue;
            List<Integer> border = border(world, front);
            if (border.isEmpty()) continue;
            Map<Integer, Integer> assignedStrength = new HashMap<>();
            for (long army : front.armies) {
                int from = world.locationOf(army);
                if (from < 0) continue; // destroyed / missing army
                int destination = -1;
                long bestScore = Long.MAX_VALUE;
                for (int province : border) {
                    long score = 100L * Math.max(0, world.distance(from, province))
                            + assignedStrength.getOrDefault(province, 0);
                    if (score < bestScore) { bestScore = score; destination = province; }
                }
                if (destination < 0) continue;
                assignedStrength.merge(destination, Math.max(1, world.armyStrength(army)), Integer::sum);
                if (from != destination) {
                    world.moveArmy(army, destination);
                } else if (front.order == Order.ATTACK
                        && world.isAtWar(front.owner, front.enemy)) {
                    for (int adjacent : world.neighborsOf(destination)) {
                        if (world.ownerOf(adjacent) == front.enemy) {
                            world.attackProvince(army, adjacent);
                            break;
                        }
                    }
                }
            }
        }
    }

    private Front required(String id) {
        Front front = fronts.get(id);
        if (front == null) throw new IllegalArgumentException("Unknown front: " + id);
        return front;
    }
}
