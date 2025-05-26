import java.util.ArrayList;
import java.util.Scanner;
import javax.sound.sampled.*;

// Main class
public class Main {
	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);

		System.out.println("Welcome to CLI Metronome!");
		System.out.println("Please enter a BPM number or type 'tap' to tap your tempo:");
		String input = scanner.nextLine().trim();

		try {
			// Tries to parse input as a whole BPM number
			int bpm = Integer.parseInt(input);
			manualBPM(bpm);
		} catch (NumberFormatException e) {
			// If the input isn't a number, check to see if they want to tap the tempo
			if (input.equalsIgnoreCase("tap")) {
				tapToBPM();
			} else {
				// Invalid input handler
				System.out.println("Invalid input. Please enter a whole number or type 'tap' to tap your tempo:");
			}
		}

	}
	// Runs the metronome with a manually entered BPM number as its parameter
	public static void manualBPM(int bpm) {
		runMetronome(bpm);
	}

	// Lets the user tap Enter 3 times, then converts the average interval to BPM based on current timestamps
	public static void tapToBPM() {
		Scanner scanner = new Scanner(System.in);
		ArrayList<Long> taps = new ArrayList<>();

		System.out.println("Tap Enter 3 times to set tempo:");

		while (taps.size() < 3) {
			String input = scanner.nextLine();
			if (input.isEmpty()) {
				long now = System.currentTimeMillis();
				// Resets the taps if the gap between taps is more than 2 seconds. Who plays that slowly?
				if (!taps.isEmpty() && now - taps.get(taps.size() - 1) > 2000) {
					System.out.println("Too much time between taps. Please start over.");
					taps.clear();
					continue;
				}
				taps.add(now);
			}
		}

		// Calculates the intervals between taps
		ArrayList<Long> intervals = new ArrayList<>();
		for (int i = 1; i < taps.size(); i++) {
			intervals.add(taps.get(i) - taps.get(i - 1));
		}

		// Calculates the average interval into a BPM int
		long total = 0;
		for (long interval : intervals) {
			total += interval;
		}
		long average = total / intervals.size();

		int bpm = (int) (60000 / average);
		System.out.println("Estimated BPM: " + bpm);

		// Runs the metronome with the averaged out tapped BPM
		runMetronome(bpm);
	}

	// Pulls the doot.wav file from resources (it's also in src) and plays it through the audio output
	public static void playDootSound() {
		try {
			AudioInputStream audioIn = AudioSystem.getAudioInputStream(
				Main.class.getResourceAsStream("/doot.wav")
			);
			Clip clip = AudioSystem.getClip();
			clip.open(audioIn);
			clip.start();
		} catch (Exception e) {
			// In case it can't find the doot.wav file
			System.out.println("Could not play sound: " + e.getMessage());
		}
	}

	// The actual metronome itself. Prints doot and plays doot.wav simultaneously at the BPM set by the parameter
	public static void runMetronome(int bpm) {
		// Delay between beats in milliseconds. 1 minute = 60,000 ms, thus we get BPM
		int delay = 60000 / bpm;
		Scanner scanner = new Scanner(System.in);

		System.out.println("Metronome started. Press Enter to stop.");

		// A thread for running the doots independantly
		Thread dootThread = new Thread(() -> {
			while (!Thread.currentThread().isInterrupted()) {
				System.out.println("Doot");
				playDootSound();
				try {
					// Sleeps for one beat
					Thread.sleep(delay);
				} catch (InterruptedException e) {
					// Exits the loop if interrupted
					break;
				}
			}
		});

		dootThread.start();
		scanner.nextLine();  // Wait for user input to stop the metronome
		dootThread.interrupt();

		System.out.println("Metronome stopped.");
	}
}
