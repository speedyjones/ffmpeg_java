import net.bramp.ffmpeg.FFmpeg;
import net.bramp.ffmpeg.FFmpegExecutor;
import net.bramp.ffmpeg.FFmpegUtils;
import net.bramp.ffmpeg.FFprobe;
import net.bramp.ffmpeg.builder.FFmpegBuilder;
import net.bramp.ffmpeg.job.FFmpegJob;
import net.bramp.ffmpeg.probe.FFmpegProbeResult;
import net.bramp.ffmpeg.progress.Progress;
import net.bramp.ffmpeg.progress.ProgressListener;

import java.util.concurrent.TimeUnit;

public class ffmpegTask {
    public static void main(String[] args) {
        try {

            FFmpeg ffmpeg = new FFmpeg("/usr/bin/ffmpeg");
            FFprobe ffprobe = new FFprobe("/usr/bin/ffprobe");

            String inputFile  = "/media/prabeer-pc/New Volume2/videos/Dashcam/DCIMA/MOVA0646.avi";
            String outputFile = "/media/prabeer-pc/New Volume2/videos/Dashcam/DCIMA/compressed/MOVA0646_Java.mp4";

            FFmpegBuilder builder = new FFmpegBuilder()

                    .setInput(inputFile.trim())     // Filename, or an FFMPEGProbeResult
                    .overrideOutputFiles(true) // Override the output if it exists
                    .addOutput(outputFile.trim())   // Filename for the destination
                    .done();

            FFmpegExecutor executor = new FFmpegExecutor(ffmpeg, ffprobe);
            FFmpegProbeResult result = ffprobe.probe(inputFile.trim());

            FFmpegJob job = executor.createJob(builder, new ProgressListener() {

                // Using the FFmpegProbeResult determine the duration of the input
                final double duration_ns = result.getFormat().duration * TimeUnit.SECONDS.toNanos(1);

                @Override
                public void progress(Progress progress) {
                    double percentage = progress.out_time_ns / duration_ns;

                    // Print out interesting information about the progress
                    System.out.println(String.format(
                            "[%.0f%%] status:%s frame:%d time:%s ms fps:%.0f speed:%.2fx",
                            percentage * 100,
                            progress.status,
                            progress.frame,
                            FFmpegUtils.toTimecode(progress.out_time_ns, TimeUnit.NANOSECONDS),
                            progress.fps.doubleValue(),
                            progress.speed
                    ));
                }
            });

            job.run();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
