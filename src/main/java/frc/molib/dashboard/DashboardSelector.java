package frc.molib.dashboard;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.util.sendable.SendableRegistry;
import edu.wpi.first.wpilibj.smartdashboard.SendableBuilderImpl;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;

/**
 * <p>Creates an option selector in dashboard</p>
 * 
 * @param <OptionEnum> Enumeration tied to the list of options.
 */
public class DashboardSelector<OptionEnum extends Enum<OptionEnum> & DashboardOptionBase> {

	private final NetworkTable mParentTable;
	private final String mKey;

	private final SendableChooser<OptionEnum> chsSendable = new SendableChooser<OptionEnum>();

	/**
	 * Constructor
	 * @param parentTable 	Parent NetworkTable
	 * @param key			Identifier key
	 * @param defaultOption	Default selected option
	 */
	public DashboardSelector(NetworkTable parentTable, String key, OptionEnum defaultOption) {
		mParentTable = parentTable;
		mKey = key;

		for(OptionEnum enumValue : defaultOption.getDeclaringClass().getEnumConstants())
			chsSendable.addOption(enumValue.getLabel(), enumValue);
		chsSendable.setDefaultOption(defaultOption.getLabel(), defaultOption);

		DashboardManager.addSelector(this);
	}

	/**
	 * Must be run at the start, <i>but after NetworkTables has connected,</i> for it to appear in NetworkTables
	 */
	public void init() {
		NetworkTable tblData = mParentTable.getSubTable(mKey);
		SendableBuilderImpl builder = new SendableBuilderImpl();
		builder.setTable(tblData);
		SendableRegistry.publish(chsSendable, builder);
		builder.startListeners();
		tblData.getEntry(".name").setString(mKey);
	}

	/**
	 * Retrieves the currently selected option in dashboard.
	 * @return Selected object. If one is not selected, returns the default option
	 */
	public OptionEnum get() { return chsSendable.getSelected(); }

	public void update() {
		SendableRegistry.update(chsSendable);
		
	}
}
