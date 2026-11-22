import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import javax.imageio.ImageIO;

/**
 * Scales a single {@code @2x} icon down to half resolution and stores the result as an RGBA PNG.
 * <p>
 * The target is the corresponding runtime location: for icons at the repo root
 * ({@code icons/{vehicles,lightsabers}/<faction>/<name>@2x.png}) this is
 * {@code src/main/resources/icons/{vehicles,lightsabers}/<faction>/<name>.png}; for files already
 * inside {@code src/main/resources/icons/} (e.g. faction logos) the result is written next to the
 * source with the {@code @2x} suffix stripped. Each dimension is halved and rounded up
 * (e.g. 109 -> 55). The output is always an RGBA PNG, regardless of the source color type, and
 * existing targets are overwritten.
 * <p>
 * Usage: {@code java .agents/skills/scale-2x-icon/scripts/ScaleIcons.java <@2x icon path>}
 */
public final class ScaleIcons {

    private static final String ICONS_ROOT = "icons";
    private static final String RUNTIME_ROOT = "src/main/resources/icons";
    private static final String SUFFIX = "@2x.png";

    public static void main(String[] args) {
        if (args.length != 1) {
            System.err.println("Usage: java ScaleIcons.java <@2x icon path>");
            System.exit(1);
        }
        try {
            Path source = Paths.get(args[0]);
            if (!Files.isRegularFile(source)) {
                throw new IllegalArgumentException("No such file: " + source);
            }
            scaleToHalfResolution(source, runtimeTarget(source));
        } catch (IllegalArgumentException | IOException e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }
    }

    private static Path runtimeTarget(Path source) {
        String normalized = source.toString().replace('\\', '/');
        if (!normalized.endsWith(SUFFIX)) {
            throw new IllegalArgumentException("Not an @2x icon: " + source);
        }

        // Files already inside the runtime icon tree (e.g. faction logos): write next to the source.
        int srcIdx = normalized.lastIndexOf("/" + RUNTIME_ROOT);
        if (srcIdx >= 0 || normalized.startsWith(RUNTIME_ROOT + "/")) {
            return Paths.get(normalized.substring(0, normalized.length() - SUFFIX.length()) + ".png");
        }

        // Repo-root icons: map into the runtime icon tree.
        int idx = normalized.lastIndexOf("/" + ICONS_ROOT + "/");
        if (idx < 0 && !normalized.startsWith(ICONS_ROOT + "/")) {
            throw new IllegalArgumentException("Not an icon under icons/: " + source);
        }

        String repoRoot = idx == -1 ? "" : normalized.substring(0, idx);
        String rest = normalized.substring(idx == -1 ? 0 : idx + 1).substring(ICONS_ROOT.length() + 1);
        String name = rest.substring(0, rest.length() - SUFFIX.length()) + ".png";
        return Paths.get((repoRoot.isEmpty() ? "" : repoRoot + "/") + RUNTIME_ROOT + "/" + name);
    }

    private static void scaleToHalfResolution(Path source, Path target) throws IOException {
        BufferedImage image = ImageIO.read(source.toFile());
        if (image == null) {
            throw new IOException("Could not read image: " + source);
        }
        int width = (image.getWidth() + 1) / 2; // round up
        int height = (image.getHeight() + 1) / 2;

        BufferedImage scaled = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics = scaled.createGraphics();
        try {
            graphics.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
            graphics.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
            graphics.drawImage(image, 0, 0, width, height, null);
        } finally {
            graphics.dispose();
        }

        Files.createDirectories(target.getParent());
        ImageIO.write(scaled, "png", target.toFile());
        System.out.printf("scaled: %s (%dx%d) -> %s (%dx%d)%n", source, image.getWidth(), image.getHeight(), target, width, height);
    }
}
