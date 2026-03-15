package legend.game.types;

public enum MessageBoxType {
  /** This MessageBox has no additional inputs and just displays a message. Maps to retail type == 0 */
  ALERT,
  /** This MessageBox is only used for Dabas and is not dismissable, avoid using. Maps to retail type == 1 */
  UNKNOWN,
  /** This MessageBox displays selectable options, typically yes/no. Maps to retail type == 2 */
  CONFIRMATION,
  ;
}
