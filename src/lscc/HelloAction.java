package lscc;

import com.intellij.notification.Notification;
import com.intellij.notification.NotificationType;
import com.intellij.notification.Notifications;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.progress.Task;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.io.StreamUtil;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

public class HelloAction extends AnAction {
	@Override public void actionPerformed(AnActionEvent event) {
		Project project = event.getProject();
//		ProjectManager.getInstance().getOpenProjects()

		Editor editor = FileEditorManager
				.getInstance(project)
				.getSelectedTextEditor();

/*		ApplicationManager.getApplication()
				.runWriteAction(() -> {
					editor.getDocument().setText(
							editor.getDocument().getText() + "\nHello"
					);
				})*/;


		new Task.Backgroundable(project, "stuff", true) {
			@Override public void run(@NotNull ProgressIndicator indicator) {
				String s = "none!!";
				try {
					Process process =
							Runtime.getRuntime().exec("sleep", new String[]{ "3", "&&", "pwd" });
					s = StreamUtil.readText(process.getInputStream());
				} catch (IOException e) {
					e.printStackTrace();
				}

				final String finalS = s;
				ApplicationManager.getApplication()
						.invokeLater(() -> {
							Notification notification = new Notification(
									"aaa",
									"LSCC",
									finalS,
									NotificationType.INFORMATION
							);
							ApplicationManager.getApplication()
									.getMessageBus()
									.syncPublisher(Notifications.TOPIC)
									.notify(notification);
						});
			}
		}.queue();


//		String s = editor.getDocument().getText().substring(0, 10);

//		FileDocumentManager.getInstance()
//				.getDocument()

	}
}
