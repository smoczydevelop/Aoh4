package com.aoh4.frontline;

import java.util.*;

/**
 * AoH3 planning core, based on the structure of the user's Android classes.dex.
 *
 * IMPORTANT: This is source code, not a ready-to-install DEX patch.
 * The caller must integrate this class with game-turn updates and menus.
 */
public final class FrontlineSystem {
    public enum Order { HOLD, DEFEND, ATTACK, STOP }

    public interface World {
        int ownerOf(int provinceId);
        Collection<Integer> neighborsOf(int provinceId);
        Collection<Integer> provincesOf(int civilizationId);
        int locationOf(int civilizationId, String armyKey);
        int armyStrength(int civilizationId, String armyKey);
        int distance(int fromProvinceId, int toProvinceId);
        boolean isMoving(int civilizationId, String armyKey);
        boolean isAtWar(int civilizationA, int civilizationB);
        boolean issueMove(int civilizationId, String armyKey, int toProvinceId, boolean offensive);
    }

    public static final class Front {
        public final String id;
        public final int owner;
        public final int enemy;
        public final String name;
        public final int colorRgb;
        public Order order = Order.HOLD;
        public final Set<String> armies = new LinkedHashSet<>();
        // Empty means the entire land border with 'enemy'.
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
        if (owner == enemy) throw new IllegalArgumentException("Choose another state");
        Front front = new Front(UUID.randomUUID().toString(), owner, enemy, name, colorRgb);
        fronts.put(front.id, front);
        return front;
    }

    public Collection<Front> fronts() {
        return Collections.unmodifiableCollection(fronts.values());
    }

    public boolean remove(String id) {
        return fronts.remove(id) != null;
    }

    public void assign(String frontId, String armyKey) {
        Front target = required(frontId);
        Objects.requireNonNull(armyKey, "armyKey");
        for (Front front : fronts.values()) {
            if (front.owner == target.owner) front.armies.remove(armyKey);
        }
        target.armies.add(armyKey);
    }

    public void setOrder(String frontId, Order order) {
        required(frontId).order = Objects.requireNonNull(order);
    }

    public void selectBorder(String frontId, Collection<Integer> provinceIds) {
        Front front = required(frontId);
        front.selectedBorderProvinces.clear();
        front.selectedBorderProvinces.addAll(provinceIds);
    }

    public List<Integer> border(World world, Front front) {
        List<Integer> result = new ArrayList<>();
        for (int province : world.provincesOf(front.owner)) {
            if (world.ownerOf(province) != front.owner) continue;
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

    /**
     * Call ONCE per turn, not per render frame.
     * Real game implementation must validate paths and ownership.
     */
    public void tick(World world) {
        for (Front front : fronts.values()) {
            if (front.order == Order.STOP) continue;
            List<Integer> positions = border(world, front);
            if (positions.isEmpty()) continue;
            Map<Integer, Integer> allocated = new HashMap<>();
            for (String key : front.armies) {
                int origin = world.locationOf(front.owner, key);
                if (origin < 0 || world.isMoving(front.owner, key)) continue;
                int destination = -1;
                long best = Long.MAX_VALUE;
                for (int province : positions) {
                    long distanceScore = Math.max(0, world.distance(origin, province));
                    long score = distanceScore * 100L + allocated.getOrDefault(province, 0);
                    if (score < best) {
                        best = score;
                        destination = province;
                    }
                }
                if (destination < 0) continue;
                allocated.merge(destination,
                        Math.max(1, world.armyStrength(front.owner, key)),
                        Integer::sum);
                if (origin != destination) {
                    world.issueMove(front.owner, key, destination, false);
                    continue;
                }
                if (front.order == Order.ATTACK && world.isAtWar(front.owner, front.enemy)) {
                    for (int adjacent : world.neighborsOf(origin)) {
                        if (world.ownerOf(adjacent) == front.enemy) {
                            world.issueMove(front.owner, key, adjacent, true);
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
