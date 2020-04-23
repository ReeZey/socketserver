package it.reez.explore.noise;

import static it.reez.explore.Main.mapWidth;
import static it.reez.explore.Main.mapHeight;

public final class GenerateNoise {
    public static Noise generateSimplexNoise(int seed, int posy, int posx){
        float[][] simplexnoise = new float[mapWidth][mapHeight];
        int offsety = 50000000 + (mapHeight * posy), offsetx = 50000000 + (mapWidth * posx);
        float frequency = 0.01f;

        for(int y = 0; y < mapHeight; y++) {
            for (int x = 0; x < mapWidth; x++) {
                simplexnoise[x][y] = noise((x+offsetx) * frequency, (y+offsety) * frequency, seed);
                simplexnoise[x][y] = (simplexnoise[x][y] + 1) / 2;   //generate values between 0 and 1
            }
        }

        return new Noise(simplexnoise);
    }

    public static float noise(float x, float y, float z)
    {
        float fx = floor(x), fy = floor(y), fz = floor(z);
        int gx = (int) fx & 0xFF, gy = (int) fy & 0xFF, gz = (int) fz & 0xFF;
        float u = fade(x -= fx), v = fade(y -= fy), w = fade(z -= fz);
        int a0 = perm[gx] + gy, b0 = perm[gx + 1] + gy, aa = perm[a0] + gz, ab = perm[a0 + 1] + gz, ba = perm[b0] + gz, bb = perm[b0 + 1] + gz;
        float a1 = grad(perm[bb + 1], x - 1, y - 1, z - 1), a2 = grad(perm[ab + 1], x - 0, y - 1, z - 1), a3 = grad(perm[ba + 1], x - 1, y - 0, z - 1), a4 = grad(perm[aa + 1], x - 0, y - 0, z - 1), a5 = grad(perm[bb], x - 1, y - 1, z - 0), a6 = grad(perm[ab], x - 0, y - 1, z - 0), a7 = grad(perm[ba], x - 1, y - 0, z - 0), a8 = grad(perm[aa], x - 0, y - 0, z - 0);
        float a2_1 = lerp(u, a2, a1), a4_3 = lerp(u, a4, a3), a6_5 = lerp(u, a6, a5), a8_7 = lerp(u, a8, a7), a8_5 = lerp(v, a8_7, a6_5), a4_1 = lerp(v, a4_3, a2_1);
        return lerp(w, a8_5, a4_1);
    }

    private static float floor(float v)
    {
        return (int) v;
    }

    private static float fade(float t)
    {
        return t * t * t * (t * (t * 6.0f - 15.0f) + 10.0f);
    }

    private static float lerp(float t, float a, float b)
    {
        return a + t * (b - a);
    }

    private static float grad(int hash, float x, float y, float z){
        int h = hash & 15;
        float u = (h < 8) ? x : y;
        float v = (h < 4) ? y : ((h == 12 || h == 14) ? x : z);
        return ((h & 1) == 0 ? u : -u) + ((h & 2) == 0 ? v : -v);
    }

    private static final int[] perm = new int[512];

    static {
        int[] permutation = { 151, 160, 137, 91, 90, 15, 131, 13, 201, 95, 96, 53, 194, 233, 7, 225, 140, 36, 103, 30, 69, 142, 8, 99, 37, 240, 21, 10, 23, 190, 6, 148, 247, 120, 234, 75, 0, 26, 197, 62, 94, 252, 219, 203, 117, 35, 11, 32, 57, 177, 33, 88, 237, 149, 56, 87, 174, 20, 125, 136, 171, 168, 68, 175, 74, 165, 71, 134, 139, 48, 27, 166, 77, 146, 158, 231, 83, 111, 229, 122, 60, 211, 133, 230, 220, 105, 92, 41, 55, 46, 245, 40, 244, 102, 143, 54, 65, 25, 63, 161, 1, 216, 80, 73, 209, 76, 132, 187, 208, 89, 18, 169, 200, 196, 135, 130, 116, 188, 159, 86, 164, 100, 109, 198, 173, 186, 3, 64, 52, 217, 226, 250, 124, 123, 5, 202, 38, 147, 118, 126, 255, 82, 85, 212, 207, 206, 59, 227, 47, 16, 58, 17, 182, 189, 28, 42, 223, 183, 170, 213, 119, 248, 152, 2, 44, 154, 163, 70, 221, 153, 101, 155, 167, 43, 172, 9, 129, 22, 39, 253, 19, 98, 108, 110, 79, 113, 224, 232, 178, 185, 112, 104, 218, 246, 97, 228, 251, 34, 242, 193, 238, 210, 144, 12, 191, 179, 162, 241, 81, 51, 145, 235, 249,
                14, 239, 107, 49, 192, 214, 31, 181, 199, 106, 157, 184, 84, 204, 176, 115, 121, 50, 45, 127, 4, 150, 254, 138, 236, 205, 93, 222, 114, 67, 29, 24, 72, 243, 141, 128, 195, 78, 66, 215, 61, 156, 180 };
        for (int i = 0; i < 256; i++)
            perm[256 + i] = perm[i] = permutation[i];
    }
}