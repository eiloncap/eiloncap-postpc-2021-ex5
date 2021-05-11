package exercise.android.reemh.todo_items


import org.junit.Assert.*
import org.junit.Test


class TodoItemsHolderImplTest {

    @Test
    fun when_addingTodoItem_then_callingListShouldHaveThisItem() {
        // setup
        val holderUnderTest = TodoItemsHolderImpl()
        assertEquals(0, holderUnderTest.getCurrentItems().size)

        // test
        holderUnderTest.addNewInProgressItem("do shopping")

        // verify
        assertEquals(1, holderUnderTest.getCurrentItems().size)
    }

    @Test
    fun when_deletingTodoItem_then_callingListShouldNotHaveThisItem() {
        // setup
        val holderUnderTest = TodoItemsHolderImpl()
        assertEquals(0, holderUnderTest.getCurrentItems().size)
        holderUnderTest.addNewInProgressItem("do shopping")

        // test
        holderUnderTest.deleteItem(holderUnderTest.getCurrentItems()[0])

        // verify
        assertEquals(0, holderUnderTest.getCurrentItems().size)
    }

    @Test
    fun when_markingTodoItemAsDone_then_callingListShouldHaveThisItemMarkedDone() {
        // setup
        val holderUnderTest = TodoItemsHolderImpl()
        assertEquals(0, holderUnderTest.getCurrentItems().size)
        holderUnderTest.addNewInProgressItem("do shopping")
        val item = holderUnderTest.getCurrentItems()[0]

        // test
        holderUnderTest.markItemDone(item)

        // verify
        assertTrue(item.isDone)
    }

    @Test
    fun when_markingTodoItemAsDoneAndThanInProgress_then_callingListShouldHaveThisItemMarkedInProgress() {
        // setup
        val holderUnderTest = TodoItemsHolderImpl()
        assertEquals(0, holderUnderTest.getCurrentItems().size)
        holderUnderTest.addNewInProgressItem("do shopping")
        val item = holderUnderTest.getCurrentItems()[0]

        // test
        holderUnderTest.markItemDone(item)
        holderUnderTest.markItemInProgress(item)

        // verify
        assertFalse(item.isDone)
    }

    @Test
    fun when_markingTodoItemTwice_then_itShouldStayTheSame() {
        // setup
        val holderUnderTest = TodoItemsHolderImpl()
        assertEquals(0, holderUnderTest.getCurrentItems().size)
        holderUnderTest.addNewInProgressItem("do shopping")
        holderUnderTest.addNewInProgressItem("do laundry")
        val items = holderUnderTest.getCurrentItems()

        // test
        holderUnderTest.markItemInProgress(items[0])
        holderUnderTest.markItemDone(items[1])
        holderUnderTest.markItemDone(items[0])
        holderUnderTest.markItemDone(items[0])
        holderUnderTest.markItemInProgress(items[1])
        holderUnderTest.markItemInProgress(items[1])

        // verify
        assertTrue(items[0].isDone)
        assertFalse(items[1].isDone)
    }

    @Test
    fun when_reachingDeletedTodoItem_then_noExceptionShouldAppear() {
        // setup
        val holderUnderTest = TodoItemsHolderImpl()
        assertEquals(0, holderUnderTest.getCurrentItems().size)
        holderUnderTest.addNewInProgressItem("do shopping")
        val item = holderUnderTest.getCurrentItems()[0]

        // test
        holderUnderTest.deleteItem(item)
        try {
            holderUnderTest.markItemInProgress(item)
            holderUnderTest.markItemDone(item)
            holderUnderTest.deleteItem(item)
        } catch (e: Exception) {
            assertFalse(true)
        }
    }

    @Test
    fun when_adding2TodoItemsWithSameString_then_callingListShouldIncludeThemAll() {
        // setup
        val holderUnderTest = TodoItemsHolderImpl()
        assertEquals(0, holderUnderTest.getCurrentItems().size)
        holderUnderTest.addNewInProgressItem("do shopping")
        holderUnderTest.addNewInProgressItem("do shopping")
        holderUnderTest.addNewInProgressItem("do shopping")
        holderUnderTest.addNewInProgressItem("do shopping")

        // verify
        assertEquals(4, holderUnderTest.getCurrentItems().size)
    }

    @Test
    fun when_changingGetCurrentItemsList_then_callingListShouldNotChange() {
        // setup
        val holderUnderTest = TodoItemsHolderImpl()
        assertEquals(0, holderUnderTest.getCurrentItems().size)
        val items = holderUnderTest.getCurrentItems()
        holderUnderTest.addNewInProgressItem("go to doctor")
        holderUnderTest.addNewInProgressItem("do shopping")

        // verify
        assertEquals(0, items.size)
    }

    @Test
    fun when_addingItemsAfterDeletingAndManipulating_then_callingListShouldBeCorrect() {
        // setup
        val holderUnderTest = TodoItemsHolderImpl()
        assertEquals(0, holderUnderTest.getCurrentItems().size)
        holderUnderTest.addNewInProgressItem("do laundry")
        holderUnderTest.addNewInProgressItem("do shopping")
        holderUnderTest.addNewInProgressItem("do shopping")
        holderUnderTest.addNewInProgressItem("do laundry")
        holderUnderTest.addNewInProgressItem("do laundry")
        holderUnderTest.addNewInProgressItem("do shopping")
        holderUnderTest.addNewInProgressItem("do laundry")
        assertEquals(7, holderUnderTest.getCurrentItems().size)
        val items = holderUnderTest.getCurrentItems()

        // test
        holderUnderTest.markItemInProgress(items[0])
        holderUnderTest.markItemDone(items[1])
        holderUnderTest.markItemInProgress(items[1])
        holderUnderTest.markItemDone(items[2])
        holderUnderTest.deleteItem(items[0])
        holderUnderTest.markItemInProgress(items[3])
        holderUnderTest.deleteItem(items[2])
        holderUnderTest.deleteItem(items[1])
        holderUnderTest.addNewInProgressItem("do laundry")
        holderUnderTest.addNewInProgressItem("do shopping")
        holderUnderTest.addNewInProgressItem("do shopping")
        holderUnderTest.addNewInProgressItem("do laundry")

        // verify
        assertEquals(8, holderUnderTest.getCurrentItems().size)
    }

    @Test
    fun when_complicatedOperationsAreCalled_then_callingListShouldBeFine1() {
        // setup
        val holderUnderTest = TodoItemsHolderImpl()
        assertEquals(0, holderUnderTest.getCurrentItems().size)
        holderUnderTest.addNewInProgressItem("do shopping")
        holderUnderTest.addNewInProgressItem("do laundry")
        holderUnderTest.addNewInProgressItem("do dishes")
        holderUnderTest.addNewInProgressItem("pay Arnona")
        assertEquals(4, holderUnderTest.getCurrentItems().size)
        val items = holderUnderTest.getCurrentItems()

        // test
        holderUnderTest.markItemInProgress(items[0])
        holderUnderTest.markItemDone(items[1])
        holderUnderTest.markItemInProgress(items[1])
        holderUnderTest.markItemDone(items[2])
        holderUnderTest.deleteItem(items[0])
        holderUnderTest.markItemInProgress(items[3])

        // verify
        assertFalse(items[0] in holderUnderTest.getCurrentItems())
        assertTrue(items[1] in holderUnderTest.getCurrentItems())
        assertTrue(items[2] in holderUnderTest.getCurrentItems())
        assertTrue(items[3] in holderUnderTest.getCurrentItems())
        assertEquals(3, holderUnderTest.getCurrentItems().size)
        assertFalse(items[1].isDone)
        assertTrue(items[2].isDone)
        assertFalse(items[3].isDone)
    }

    @Test
    fun when_complicatedOperationsAreCalled_then_callingListShouldBeFine2() {
        // setup
        val holderUnderTest = TodoItemsHolderImpl()
        assertEquals(0, holderUnderTest.getCurrentItems().size)
        holderUnderTest.addNewInProgressItem("do dishes")
        holderUnderTest.addNewInProgressItem("do dishes")
        holderUnderTest.addNewInProgressItem("do shopping")
        holderUnderTest.addNewInProgressItem("do dishes")
        holderUnderTest.addNewInProgressItem("pay Arnona")
        holderUnderTest.addNewInProgressItem("do laundry")
        holderUnderTest.addNewInProgressItem("do dishes")
        holderUnderTest.addNewInProgressItem("pay Arnona")
        assertEquals(8, holderUnderTest.getCurrentItems().size)
        var items = holderUnderTest.getCurrentItems()

        // test
        holderUnderTest.markItemInProgress(items[0])
        holderUnderTest.markItemDone(items[1])
        holderUnderTest.markItemInProgress(items[1])
        holderUnderTest.markItemDone(items[2])
        holderUnderTest.deleteItem(items[0])
        holderUnderTest.markItemInProgress(items[3])
        holderUnderTest.deleteItem(items[2])
        holderUnderTest.addNewInProgressItem("pay Arnona")
        holderUnderTest.addNewInProgressItem("pay Arnona")
        holderUnderTest.addNewInProgressItem("pay Arnona")
        holderUnderTest.addNewInProgressItem("pay Arnona")
        items = holderUnderTest.getCurrentItems()
        holderUnderTest.markItemInProgress(items[0])
        holderUnderTest.markItemDone(items[0])
        holderUnderTest.markItemInProgress(items[0])
        holderUnderTest.markItemDone(items[0])

        // verify
        assertTrue(items[0].isDone)
    }
}
