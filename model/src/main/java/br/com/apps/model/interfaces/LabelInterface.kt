package br.com.apps.model.interfaces

import br.com.apps.model.exceptions.null_objects.NullLabelException
import br.com.apps.model.model.label.Label

/**
 * An interface to manage labels associated with objects that contain a [Label] as one of their properties.
 */
interface LabelInterface {

    /**
     * Sets the [label] property of object based on the provided list of labels.
     * @param labels A list of labels objects to search for the label with the matching ID.
     * @throws NullLabelException If no label in the label list has an ID that matches the [labelId]
     * of this object.
     */
    fun setLabelById(labels: List<Label>)

    /**
     * @return The name of the [Label] or the string "-" as error if the customer is not registered.
     */
    fun getLabelName(): String

}