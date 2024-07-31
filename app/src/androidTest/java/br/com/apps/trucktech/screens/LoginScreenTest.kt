package br.com.apps.trucktech.screens

class LoginScreenTest { }
/*

    @get: Rule
    val rule = ActivityScenarioRule(LoginActivity::class.java)

    private lateinit var mockVm: AuthViewModel
    private lateinit var authRepo: AuthenticationRepository
    private val fbAuth = mockk<FirebaseAuth>()

    @Before
    fun setup() {
        authRepo = AuthenticationRepository(fbAuth)
        mockVm = AuthViewModel(authRepo)
    }

    //---------------------------------------------------------------------------------------------//
    // getType()
    //---------------------------------------------------------------------------------------------//

    @Test
    fun shouldBeVisibleWhenAppStarts() {
        checkVisibility(R.id.frag_login_logo_image, ViewMatchers.Visibility.VISIBLE)
        //checkVisibility(R.id.frag_login_loading, ViewMatchers.Visibility.VISIBLE)
        checkVisibility(R.id.frag_login_title, ViewMatchers.Visibility.GONE)
        checkVisibility(R.id.frag_layout_user, ViewMatchers.Visibility.GONE)
        checkVisibility(R.id.frag_layout_password, ViewMatchers.Visibility.GONE)
        checkVisibility(R.id.frag_button_login, ViewMatchers.Visibility.GONE)
        checkVisibility(R.id.frag_button_help, ViewMatchers.Visibility.GONE)
    }

    private fun checkVisibility(viewId: Int, visibility: ViewMatchers.Visibility) {
        onView(withId(viewId)).check(
            ViewAssertions.matches(
                withEffectiveVisibility(visibility)
            )
        )
    }

    @Test
    fun shouldShowAnimationWhenCurrentUserIsFound() {
        every { mockVm.getCurrentUser() } returns mockk {
            if(this == null) {

            } else {
                //testShowOpeningToMainActivity()
            }
        }
    }

    private suspend fun testShowOpeningToMainActivity() {
        checkVisibility(R.id.frag_login_logo_image, ViewMatchers.Visibility.VISIBLE)
        checkVisibility(R.id.frag_login_loading, ViewMatchers.Visibility.VISIBLE)
        delay(1750)
        checkVisibility(R.id.frag_login_loading, ViewMatchers.Visibility.GONE)
        checkVisibility(R.id.frag_login_logo_image, ViewMatchers.Visibility.GONE)
    }

    */
/* @Test
     fun shouldShowAnimationWhenCurrentUserIsNotFound() {
         every { mockVm.getCurrentUser() } returns mockk {
             this = null
             Log.d("teste", "shouldShowAnimationWhenUserLoggedIn: ")
         }
     }*//*


    */
/* @Test
     fun shouldTryToLogin() {
         onView(withId(R.id.frag_field_user))
             .perform(
                 ViewActions.typeText("123@hotmail.com"),
                 ViewActions.closeSoftKeyboard()
             )
         onView(withId(R.id.frag_field_password))
             .perform(
                 ViewActions.typeText("123456"),
                 ViewActions.closeSoftKeyboard()
             )
         onView(withId(R.id.frag_button_login)).perform(ViewActions.click())
     }


}*/