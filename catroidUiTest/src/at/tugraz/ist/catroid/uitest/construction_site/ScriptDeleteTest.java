/**
 *  Catroid: An on-device graphical programming language for Android devices
 *  Copyright (C) 2010  Catroid development team 
 *  (<http://code.google.com/p/catroid/wiki/Credits>)
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package at.tugraz.ist.catroid.uitest.construction_site;

import java.util.ArrayList;

import android.test.ActivityInstrumentationTestCase2;
import at.tugraz.ist.catroid.R;
import at.tugraz.ist.catroid.constructionSite.content.ProjectManager;
import at.tugraz.ist.catroid.content.brick.Brick;
import at.tugraz.ist.catroid.content.brick.HideBrick;
import at.tugraz.ist.catroid.content.brick.ScaleCostumeBrick;
import at.tugraz.ist.catroid.content.brick.ShowBrick;
import at.tugraz.ist.catroid.content.project.Project;
import at.tugraz.ist.catroid.content.script.Script;
import at.tugraz.ist.catroid.content.sprite.Sprite;
import at.tugraz.ist.catroid.ui.ScriptActivity;

import com.jayway.android.robotium.solo.Solo;

public class ScriptDeleteTest extends ActivityInstrumentationTestCase2<ScriptActivity> {
	private Solo solo;
	private ArrayList<Brick> brickListToCheck;

	public ScriptDeleteTest() {
		super(ScriptActivity.class);

	}

	@Override
	public void setUp() throws Exception {
		createTestProject("testProject");
		solo = new Solo(getInstrumentation(), getActivity());
		super.setUp();

	}

	@Override
	public void tearDown() throws Exception {
		try {
			solo.finalize();
		} catch (Throwable e) {
			e.printStackTrace();
		}
		getActivity().finish();

		super.tearDown();
	}
	
	public void testDeleteScript() throws InterruptedException {
		solo.clickOnButton(1);
		solo.scrollDownList(0);
		solo.scrollDownList(0);
		solo.clickOnText(getActivity().getString(R.string.touched_main_adapter));
		
		solo.clickLongOnText(getActivity().getString(R.string.touched_main_adapter));
		solo.clickOnText(getActivity().getString(R.string.delete_script_button));
		Thread.sleep(1000);

		int numberOfScripts = ProjectManager.getInstance().getCurrentSprite().getScriptList().size();
		assertEquals("Incorrect number of scripts in scriptList", 1, numberOfScripts);
		assertEquals("Incorrect number of elements in listView", 4, solo.getCurrentListViews().get(0).getChildCount());
		
		
		solo.clickLongOnText(getActivity().getString(R.string.started_main_adapter));
		solo.clickOnText(getActivity().getString(R.string.delete_script_button));
		Thread.sleep(1000);
		
		numberOfScripts = ProjectManager.getInstance().getCurrentSprite().getScriptList().size();
		assertEquals("Incorrect number of scripts in list", 0, numberOfScripts);
		assertEquals("Incorrect number of elements in listView", 0, solo.getCurrentListViews().get(0).getChildCount());

		
		solo.clickOnButton(1);
		solo.scrollUpList(0);
		solo.scrollUpList(0);
		solo.clickOnText(getActivity().getString(R.string.hide_main_adapter));
		Thread.sleep(1000);
		
		numberOfScripts = ProjectManager.getInstance().getCurrentSprite().getScriptList().size();
		assertEquals("Incorrect number of scripts in scriptList", 1, numberOfScripts);
		assertEquals("Incorrect number of elements in listView", 2, solo.getCurrentListViews().get(0).getChildCount());


	}

	private void createTestProject(String projectName) {

		double scaleValue = 0.8;

		Project project = new Project(null, projectName);
		Sprite firstSprite = new Sprite("cat");

		Script testScript = new Script("testscript", firstSprite);

		brickListToCheck = new ArrayList<Brick>();
		brickListToCheck.add(new HideBrick(firstSprite));
		brickListToCheck.add(new ShowBrick(firstSprite));
		brickListToCheck.add(new ScaleCostumeBrick(firstSprite, scaleValue));
		

		// adding Bricks: ----------------
		for (Brick brick : brickListToCheck) {
			testScript.addBrick(brick);
		}
		// -------------------------------

		firstSprite.getScriptList().add(testScript);

		project.addSprite(firstSprite);

		ProjectManager.getInstance().setProject(project);
		ProjectManager.getInstance().setCurrentSprite(firstSprite);
		ProjectManager.getInstance().setCurrentScript(testScript);

	}

}