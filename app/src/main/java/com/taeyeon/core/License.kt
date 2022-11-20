package com.taeyeon.core

import com.taeyeon.dongdae.R
import java.io.ByteArrayOutputStream

/*
 * NOTICE
 *
 * Add "dependencies { classpath "com.google.android.gms:oss-licenses-plugin:0.10.4" }" at buildscript {} at build.gradle (project)
 * Add "id "com.google.android.gms.oss-licenses-plugin"" at plugins {} at build.gradle (app)
 *
 */

object License {
    data class License(val title: String, val license: String?, val link: String?)

    val Licenses by lazy {
        val rawTitles: String
        val rawTitlesInputStream = Core.getContext().resources.openRawResource(R.raw.third_party_license_metadata)
        val rawTitlesByteArrayOutputStream = ByteArrayOutputStream()
        var rawTitlesIndex: Int = rawTitlesInputStream.read()
        while (rawTitlesIndex != -1) {
            rawTitlesByteArrayOutputStream.write(rawTitlesIndex)
            rawTitlesIndex = rawTitlesInputStream.read()
        }
        rawTitles = String(rawTitlesByteArrayOutputStream.toByteArray(), Charsets.UTF_8)
        rawTitlesInputStream.close()

        val rawLicenses: String
        val rawLicensesInputStream = Core.getContext().resources.openRawResource(R.raw.third_party_licenses)
        val rawLicensesByteArrayOutputStream = ByteArrayOutputStream()
        var rawLicensesIndex: Int = rawLicensesInputStream.read()
        while (rawLicensesIndex != -1) {
            rawLicensesByteArrayOutputStream.write(rawLicensesIndex)
            rawLicensesIndex = rawLicensesInputStream.read()
        }
        rawLicenses = String(rawLicensesByteArrayOutputStream.toByteArray(), Charsets.UTF_8)

        val licenses = mutableListOf<License>()
        var isKotlinSkipped = false // OSS Licenses Plugin 0.10.4 Bug (When applied with firebase)
        rawTitles.split("\n").forEach { rawTitle ->
            if (rawTitle.isNotEmpty()) {
                val range = rawTitle.split(" ")[0]
                val rangeStart = range.split(":")[0].toInt()
                val rangeEnd = range.split(":")[1].toInt()

                val rawLicense = rawLicenses.substring(
                    if (isKotlinSkipped) rangeStart - 16 until (rangeStart + rangeEnd - 16)
                    else rangeStart until (rangeStart + rangeEnd)
                ).trim()
                val isLicenseLink = (rawLicense.startsWith("http://") || rawLicense.startsWith("https://")) && rawLicense.indexOf(".") != -1 && rawLicense.indexOf("\n") == -1

                licenses.add(
                    License(
                        title = rawTitle.split(" ").subList(1, rawTitle.split(" ").size).joinToString(" "),
                        license =
                        if (isLicenseLink) {
                            when (rawLicense) {
                                "http://www.apache.org/licenses/LICENSE-2.0.txt", "https://www.apache.org/licenses/LICENSE-2.0.txt" -> APACHE_LICENSE_2_0
                                "http://www.eclipse.org/legal/epl-v10.html" -> ECLIPSE_PUBLIC_LICENSE_1_0
                                "https://jsoup.org/license" -> JSOUP_LICENSE
                                "https://developer.android.com/studio/terms.html" -> THE_ANDROID_SOFTWARE_DEVELOPMENT_KIT_LICENSE_AGREEMENT
                                else -> rawLicense
                            }
                        } else {
                            rawLicense
                        },
                        link = if (isLicenseLink) rawLicense else null
                    )
                )

                if(!isKotlinSkipped) isKotlinSkipped = licenses.last().title.trim() == "Kotlin"
            }
        }

        licenses.sortedWith(compareBy { it.title }).toList()
    }

    private val APACHE_LICENSE_2_0 = """
                    
                                                     Apache License
                                               Version 2.0, January 2004
                                            http://www.apache.org/licenses/

                       TERMS AND CONDITIONS FOR USE, REPRODUCTION, AND DISTRIBUTION

                       1. Definitions.

                          "License" shall mean the terms and conditions for use, reproduction,
                          and distribution as defined by Sections 1 through 9 of this document.

                          "Licensor" shall mean the copyright owner or entity authorized by
                          the copyright owner that is granting the License.

                          "Legal Entity" shall mean the union of the acting entity and all
                          other entities that control, are controlled by, or are under common
                          control with that entity. For the purposes of this definition,
                          "control" means (i) the power, direct or indirect, to cause the
                          direction or management of such entity, whether by contract or
                          otherwise, or (ii) ownership of fifty percent (50%) or more of the
                          outstanding shares, or (iii) beneficial ownership of such entity.

                          "You" (or "Your") shall mean an individual or Legal Entity
                          exercising permissions granted by this License.

                          "Source" form shall mean the preferred form for making modifications,
                          including but not limited to software source code, documentation
                          source, and configuration files.

                          "Object" form shall mean any form resulting from mechanical
                          transformation or translation of a Source form, including but
                          not limited to compiled object code, generated documentation,
                          and conversions to other media types.

                          "Work" shall mean the work of authorship, whether in Source or
                          Object form, made available under the License, as indicated by a
                          copyright notice that is included in or attached to the work
                          (an example is provided in the Appendix below).

                          "Derivative Works" shall mean any work, whether in Source or Object
                          form, that is based on (or derived from) the Work and for which the
                          editorial revisions, annotations, elaborations, or other modifications
                          represent, as a whole, an original work of authorship. For the purposes
                          of this License, Derivative Works shall not include works that remain
                          separable from, or merely link (or bind by name) to the interfaces of,
                          the Work and Derivative Works thereof.

                          "Contribution" shall mean any work of authorship, including
                          the original version of the Work and any modifications or additions
                          to that Work or Derivative Works thereof, that is intentionally
                          submitted to Licensor for inclusion in the Work by the copyright owner
                          or by an individual or Legal Entity authorized to submit on behalf of
                          the copyright owner. For the purposes of this definition, "submitted"
                          means any form of electronic, verbal, or written communication sent
                          to the Licensor or its representatives, including but not limited to
                          communication on electronic mailing lists, source code control systems,
                          and issue tracking systems that are managed by, or on behalf of, the
                          Licensor for the purpose of discussing and improving the Work, but
                          excluding communication that is conspicuously marked or otherwise
                          designated in writing by the copyright owner as "Not a Contribution."

                          "Contributor" shall mean Licensor and any individual or Legal Entity
                          on behalf of whom a Contribution has been received by Licensor and
                          subsequently incorporated within the Work.

                       2. Grant of Copyright License. Subject to the terms and conditions of
                          this License, each Contributor hereby grants to You a perpetual,
                          worldwide, non-exclusive, no-charge, royalty-free, irrevocable
                          copyright license to reproduce, prepare Derivative Works of,
                          publicly display, publicly perform, sublicense, and distribute the
                          Work and such Derivative Works in Source or Object form.

                       3. Grant of Patent License. Subject to the terms and conditions of
                          this License, each Contributor hereby grants to You a perpetual,
                          worldwide, non-exclusive, no-charge, royalty-free, irrevocable
                          (except as stated in this section) patent license to make, have made,
                          use, offer to sell, sell, import, and otherwise transfer the Work,
                          where such license applies only to those patent claims licensable
                          by such Contributor that are necessarily infringed by their
                          Contribution(s) alone or by combination of their Contribution(s)
                          with the Work to which such Contribution(s) was submitted. If You
                          institute patent litigation against any entity (including a
                          cross-claim or counterclaim in a lawsuit) alleging that the Work
                          or a Contribution incorporated within the Work constitutes direct
                          or contributory patent infringement, then any patent licenses
                          granted to You under this License for that Work shall terminate
                          as of the date such litigation is filed.

                       4. Redistribution. You may reproduce and distribute copies of the
                          Work or Derivative Works thereof in any medium, with or without
                          modifications, and in Source or Object form, provided that You
                          meet the following conditions:

                          (a) You must give any other recipients of the Work or
                              Derivative Works a copy of this License; and

                          (b) You must cause any modified files to carry prominent notices
                              stating that You changed the files; and

                          (c) You must retain, in the Source form of any Derivative Works
                              that You distribute, all copyright, patent, trademark, and
                              attribution notices from the Source form of the Work,
                              excluding those notices that do not pertain to any part of
                              the Derivative Works; and

                          (d) If the Work includes a "NOTICE" text file as part of its
                              distribution, then any Derivative Works that You distribute must
                              include a readable copy of the attribution notices contained
                              within such NOTICE file, excluding those notices that do not
                              pertain to any part of the Derivative Works, in at least one
                              of the following places: within a NOTICE text file distributed
                              as part of the Derivative Works; within the Source form or
                              documentation, if provided along with the Derivative Works; or,
                              within a display generated by the Derivative Works, if and
                              wherever such third-party notices normally appear. The contents
                              of the NOTICE file are for informational purposes only and
                              do not modify the License. You may add Your own attribution
                              notices within Derivative Works that You distribute, alongside
                              or as an addendum to the NOTICE text from the Work, provided
                              that such additional attribution notices cannot be construed
                              as modifying the License.

                          You may add Your own copyright statement to Your modifications and
                          may provide additional or different license terms and conditions
                          for use, reproduction, or distribution of Your modifications, or
                          for any such Derivative Works as a whole, provided Your use,
                          reproduction, and distribution of the Work otherwise complies with
                          the conditions stated in this License.

                       5. Submission of Contributions. Unless You explicitly state otherwise,
                          any Contribution intentionally submitted for inclusion in the Work
                          by You to the Licensor shall be under the terms and conditions of
                          this License, without any additional terms or conditions.
                          Notwithstanding the above, nothing herein shall supersede or modify
                          the terms of any separate license agreement you may have executed
                          with Licensor regarding such Contributions.

                       6. Trademarks. This License does not grant permission to use the trade
                          names, trademarks, service marks, or product names of the Licensor,
                          except as required for reasonable and customary use in describing the
                          origin of the Work and reproducing the content of the NOTICE file.

                       7. Disclaimer of Warranty. Unless required by applicable law or
                          agreed to in writing, Licensor provides the Work (and each
                          Contributor provides its Contributions) on an "AS IS" BASIS,
                          WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
                          implied, including, without limitation, any warranties or conditions
                          of TITLE, NON-INFRINGEMENT, MERCHANTABILITY, or FITNESS FOR A
                          PARTICULAR PURPOSE. You are solely responsible for determining the
                          appropriateness of using or redistributing the Work and assume any
                          risks associated with Your exercise of permissions under this License.

                       8. Limitation of Liability. In no event and under no legal theory,
                          whether in tort (including negligence), contract, or otherwise,
                          unless required by applicable law (such as deliberate and grossly
                          negligent acts) or agreed to in writing, shall any Contributor be
                          liable to You for damages, including any direct, indirect, special,
                          incidental, or consequential damages of any character arising as a
                          result of this License or out of the use or inability to use the
                          Work (including but not limited to damages for loss of goodwill,
                          work stoppage, computer failure or malfunction, or any and all
                          other commercial damages or losses), even if such Contributor
                          has been advised of the possibility of such damages.

                       9. Accepting Warranty or Additional Liability. While redistributing
                          the Work or Derivative Works thereof, You may choose to offer,
                          and charge a fee for, acceptance of support, warranty, indemnity,
                          or other liability obligations and/or rights consistent with this
                          License. However, in accepting such obligations, You may act only
                          on Your own behalf and on Your sole responsibility, not on behalf
                          of any other Contributor, and only if You agree to indemnify,
                          defend, and hold each Contributor harmless for any liability
                          incurred by, or claims asserted against, such Contributor by reason
                          of your accepting any such warranty or additional liability.

                       END OF TERMS AND CONDITIONS

                       APPENDIX: How to apply the Apache License to your work.

                          To apply the Apache License to your work, attach the following
                          boilerplate notice, with the fields enclosed by brackets "[]"
                          replaced with your own identifying information. (Don't include
                          the brackets!)  The text should be enclosed in the appropriate
                          comment syntax for the file format. We also recommend that a
                          file or class name and description of purpose be included on the
                          same "printed page" as the copyright notice for easier
                          identification within third-party archives.

                       Copyright [yyyy] [name of copyright owner]

                       Licensed under the Apache License, Version 2.0 (the "License");
                       you may not use this file except in compliance with the License.
                       You may obtain a copy of the License at

                           http://www.apache.org/licenses/LICENSE-2.0

                       Unless required by applicable law or agreed to in writing, software
                       distributed under the License is distributed on an "AS IS" BASIS,
                       WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
                       See the License for the specific language governing permissions and
                       limitations under the License.
                """.trimIndent()

    val ECLIPSE_PUBLIC_LICENSE_1_0 = """
        Eclipse Public License - v 1.0

        THE ACCOMPANYING PROGRAM IS PROVIDED UNDER THE TERMS OF THIS ECLIPSE PUBLIC LICENSE ("AGREEMENT"). ANY USE, REPRODUCTION OR DISTRIBUTION OF THE PROGRAM CONSTITUTES RECIPIENT'S ACCEPTANCE OF THIS AGREEMENT.

        1. DEFINITIONS
        "Contribution" means:
        a) in the case of the initial Contributor, the initial code and documentation distributed under this Agreement, and
        b) in the case of each subsequent Contributor:
        i) changes to the Program, and
        ii) additions to the Program;
        where such changes and/or additions to the Program originate from and are distributed by that particular Contributor. A Contribution 'originates' from a Contributor if it was added to the Program by such Contributor itself or anyone acting on such Contributor's behalf. Contributions do not include additions to the Program which: (i) are separate modules of software distributed in conjunction with the Program under their own license agreement, and (ii) are not derivative works of the Program.
        "Contributor" means any person or entity that distributes the Program.
        "Licensed Patents" mean patent claims licensable by a Contributor which are necessarily infringed by the use or sale of its Contribution alone or when combined with the Program.
        "Program" means the Contributions distributed in accordance with this Agreement.
        "Recipient" means anyone who receives the Program under this Agreement, including all Contributors.

        2. GRANT OF RIGHTS
        a) Subject to the terms of this Agreement, each Contributor hereby grants Recipient a non-exclusive, worldwide, royalty-free copyright license to reproduce, prepare derivative works of, publicly display, publicly perform, distribute and sublicense the Contribution of such Contributor, if any, and such derivative works, in source code and object code form.
        b) Subject to the terms of this Agreement, each Contributor hereby grants Recipient a non-exclusive, worldwide, royalty-free patent license under Licensed Patents to make, use, sell, offer to sell, import and otherwise transfer the Contribution of such Contributor, if any, in source code and object code form. This patent license shall apply to the combination of the Contribution and the Program if, at the time the Contribution is added by the Contributor, such addition of the Contribution causes such combination to be covered by the Licensed Patents. The patent license shall not apply to any other combinations which include the Contribution. No hardware per se is licensed hereunder.
        c) Recipient understands that although each Contributor grants the licenses to its Contributions set forth herein, no assurances are provided by any Contributor that the Program does not infringe the patent or other intellectual property rights of any other entity. Each Contributor disclaims any liability to Recipient for claims brought by any other entity based on infringement of intellectual property rights or otherwise. As a condition to exercising the rights and licenses granted hereunder, each Recipient hereby assumes sole responsibility to secure any other intellectual property rights needed, if any. For example, if a third party patent license is required to allow Recipient to distribute the Program, it is Recipient's responsibility to acquire that license before distributing the Program.
        d) Each Contributor represents that to its knowledge it has sufficient copyright rights in its Contribution, if any, to grant the copyright license set forth in this Agreement.

        3. REQUIREMENTS
        A Contributor may choose to distribute the Program in object code form under its own license agreement, provided that:
        a) it complies with the terms and conditions of this Agreement; and
        b) its license agreement:
        i) effectively disclaims on behalf of all Contributors all warranties and conditions, express and implied, including warranties or conditions of title and non-infringement, and implied warranties or conditions of merchantability and fitness for a particular purpose;
        ii) effectively excludes on behalf of all Contributors all liability for damages, including direct, indirect, special, incidental and consequential damages, such as lost profits;
        iii) states that any provisions which differ from this Agreement are offered by that Contributor alone and not by any other party; and
        iv) states that source code for the Program is available from such Contributor, and informs licensees how to obtain it in a reasonable manner on or through a medium customarily used for software exchange.
        When the Program is made available in source code form:
        a) it must be made available under this Agreement; and
        b) a copy of this Agreement must be included with each copy of the Program.
        Contributors may not remove or alter any copyright notices contained within the Program.
        Each Contributor must identify itself as the originator of its Contribution, if any, in a manner that reasonably allows subsequent Recipients to identify the originator of the Contribution.

        4. COMMERCIAL DISTRIBUTION
        Commercial distributors of software may accept certain responsibilities with respect to end users, business partners and the like. While this license is intended to facilitate the commercial use of the Program, the Contributor who includes the Program in a commercial product offering should do so in a manner which does not create potential liability for other Contributors. Therefore, if a Contributor includes the Program in a commercial product offering, such Contributor ("Commercial Contributor") hereby agrees to defend and indemnify every other Contributor ("Indemnified Contributor") against any losses, damages and costs (collectively "Losses") arising from claims, lawsuits and other legal actions brought by a third party against the Indemnified Contributor to the extent caused by the acts or omissions of such Commercial Contributor in connection with its distribution of the Program in a commercial product offering. The obligations in this section do not apply to any claims or Losses relating to any actual or alleged intellectual property infringement. In order to qualify, an Indemnified Contributor must: a) promptly notify the Commercial Contributor in writing of such claim, and b) allow the Commercial Contributor to control, and cooperate with the Commercial Contributor in, the defense and any related settlement negotiations. The Indemnified Contributor may participate in any such claim at its own expense.
        For example, a Contributor might include the Program in a commercial product offering, Product X. That Contributor is then a Commercial Contributor. If that Commercial Contributor then makes performance claims, or offers warranties related to Product X, those performance claims and warranties are such Commercial Contributor's responsibility alone. Under this section, the Commercial Contributor would have to defend claims against the other Contributors related to those performance claims and warranties, and if a court requires any other Contributor to pay any damages as a result, the Commercial Contributor must pay those damages.

        5. NO WARRANTY
        EXCEPT AS EXPRESSLY SET FORTH IN THIS AGREEMENT, THE PROGRAM IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, EITHER EXPRESS OR IMPLIED INCLUDING, WITHOUT LIMITATION, ANY WARRANTIES OR CONDITIONS OF TITLE, NON-INFRINGEMENT, MERCHANTABILITY OR FITNESS FOR A PARTICULAR PURPOSE. Each Recipient is solely responsible for determining the appropriateness of using and distributing the Program and assumes all risks associated with its exercise of rights under this Agreement , including but not limited to the risks and costs of program errors, compliance with applicable laws, damage to or loss of data, programs or equipment, and unavailability or interruption of operations.

        6. DISCLAIMER OF LIABILITY
        EXCEPT AS EXPRESSLY SET FORTH IN THIS AGREEMENT, NEITHER RECIPIENT NOR ANY CONTRIBUTORS SHALL HAVE ANY LIABILITY FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING WITHOUT LIMITATION LOST PROFITS), HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OR DISTRIBUTION OF THE PROGRAM OR THE EXERCISE OF ANY RIGHTS GRANTED HEREUNDER, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGES.

        7. GENERAL
        If any provision of this Agreement is invalid or unenforceable under applicable law, it shall not affect the validity or enforceability of the remainder of the terms of this Agreement, and without further action by the parties hereto, such provision shall be reformed to the minimum extent necessary to make such provision valid and enforceable.
        If Recipient institutes patent litigation against any entity (including a cross-claim or counterclaim in a lawsuit) alleging that the Program itself (excluding combinations of the Program with other software or hardware) infringes such Recipient's patent(s), then such Recipient's rights granted under Section 2(b) shall terminate as of the date such litigation is filed.
        All Recipient's rights under this Agreement shall terminate if it fails to comply with any of the material terms or conditions of this Agreement and does not cure such failure in a reasonable period of time after becoming aware of such noncompliance. If all Recipient's rights under this Agreement terminate, Recipient agrees to cease use and distribution of the Program as soon as reasonably practicable. However, Recipient's obligations under this Agreement and any licenses granted by Recipient relating to the Program shall continue and survive.
        Everyone is permitted to copy and distribute copies of this Agreement, but in order to avoid inconsistency the Agreement is copyrighted and may only be modified in the following manner. The Agreement Steward reserves the right to publish new versions (including revisions) of this Agreement from time to time. No one other than the Agreement Steward has the right to modify this Agreement. The Eclipse Foundation is the initial Agreement Steward. The Eclipse Foundation may assign the responsibility to serve as the Agreement Steward to a suitable separate entity. Each new version of the Agreement will be given a distinguishing version number. The Program (including Contributions) may always be distributed subject to the version of the Agreement under which it was received. In addition, after a new version of the Agreement is published, Contributor may elect to distribute the Program (including its Contributions) under the new version. Except as expressly stated in Sections 2(a) and 2(b) above, Recipient receives no rights or licenses to the intellectual property of any Contributor under this Agreement, whether expressly, by implication, estoppel or otherwise. All rights in the Program not expressly granted under this Agreement are reserved.
        This Agreement is governed by the laws of the State of New York and the intellectual property laws of the United States of America. No party to this Agreement will bring a legal action under this Agreement more than one year after the cause of action arose. Each party waives its rights to a jury trial in any resulting litigation.
    """.trimIndent()

    private val JSOUP_LICENSE = """
        The MIT License
        Copyright (c) 2009-2022 Jonathan Hedley <https://jsoup.org/>
        Permission is hereby granted, free of charge, to any person obtaining a copy
        of this software and associated documentation files (the "Software"), to deal
        in the Software without restriction, including without limitation the rights
        to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
        copies of the Software, and to permit persons to whom the Software is
        furnished to do so, subject to the following conditions:
        The above copyright notice and this permission notice shall be included in all
        copies or substantial portions of the Software.
        THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
        IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
        FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
        AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
        LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
        OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
        SOFTWARE.
    """.trimIndent()

    private val THE_ANDROID_SOFTWARE_DEVELOPMENT_KIT_LICENSE_AGREEMENT = """
        Terms and conditions

        This is the Android Software Development Kit License Agreement


        1. Introduction

        1.1 The Android Software Development Kit (referred to in the License Agreement as the "SDK" and specifically including the Android system files, packaged APIs, and Google APIs add-ons) is licensed to you subject to the terms of the License Agreement. The License Agreement forms a legally binding contract between you and Google in relation to your use of the SDK.
        1.2 "Android" means the Android software stack for devices, as made available under the Android Open Source Project, which is located at the following URL: https://source.android.com/, as updated from time to time.
        1.3 A "compatible implementation" means any Android device that (i) complies with the Android Compatibility Definition document, which can be found at the Android compatibility website (https://source.android.com/compatibility) and which may be updated from time to time; and (ii) successfully passes the Android Compatibility Test Suite (CTS).
        1.4 "Google" means Google LLC, organized under the laws of the State of Delaware, USA, and operating under the laws of the USA with principal place of business at 1600 Amphitheatre Parkway, Mountain View, CA 94043, USA.


        2. Accepting this License Agreement

        2.1 In order to use the SDK, you must first agree to the License Agreement. You may not use the SDK if you do not accept the License Agreement.
        2.2 By clicking to accept and/or using this SDK, you hereby agree to the terms of the License Agreement.
        2.3 You may not use the SDK and may not accept the License Agreement if you are a person barred from receiving the SDK under the laws of the United States or other countries, including the country in which you are resident or from which you use the SDK.
        2.4 If you are agreeing to be bound by the License Agreement on behalf of your employer or other entity, you represent and warrant that you have full legal authority to bind your employer or such entity to the License Agreement. If you do not have the requisite authority, you may not accept the License Agreement or use the SDK on behalf of your employer or other entity.


        3. SDK License from Google

        3.1 Subject to the terms of the License Agreement, Google grants you a limited, worldwide, royalty-free, non-assignable, non-exclusive, and non-sublicensable license to use the SDK solely to develop applications for compatible implementations of Android.
        3.2 You may not use this SDK to develop applications for other platforms (including non-compatible implementations of Android) or to develop another SDK. You are of course free to develop applications for other platforms, including non-compatible implementations of Android, provided that this SDK is not used for that purpose.
        3.3 You agree that Google or third parties own all legal right, title and interest in and to the SDK, including any Intellectual Property Rights that subsist in the SDK. "Intellectual Property Rights" means any and all rights under patent law, copyright law, trade secret law, trademark law, and any and all other proprietary rights. Google reserves all rights not expressly granted to you.
        3.4 You may not use the SDK for any purpose not expressly permitted by the License Agreement.  Except to the extent required by applicable third party licenses, you may not copy (except for backup purposes), modify, adapt, redistribute, decompile, reverse engineer, disassemble, or create derivative works of the SDK or any part of the SDK.
        3.5 Use, reproduction and distribution of components of the SDK licensed under an open source software license are governed solely by the terms of that open source software license and not the License Agreement.
        3.6 You agree that the form and nature of the SDK that Google provides may change without prior notice to you and that future versions of the SDK may be incompatible with applications developed on previous versions of the SDK. You agree that Google may stop (permanently or temporarily) providing the SDK (or any features within the SDK) to you or to users generally at Google's sole discretion, without prior notice to you.
        3.7 Nothing in the License Agreement gives you a right to use any of Google's trade names, trademarks, service marks, logos, domain names, or other distinctive brand features.
        3.8 You agree that you will not remove, obscure, or alter any proprietary rights notices (including copyright and trademark notices) that may be affixed to or contained within the SDK.


        4. Use of the SDK by You

        4.1 Google agrees that it obtains no right, title or interest from you (or your licensors) under the License Agreement in or to any software applications that you develop using the SDK, including any intellectual property rights that subsist in those applications.
        4.2 You agree to use the SDK and write applications only for purposes that are permitted by (a) the License Agreement and (b) any applicable law, regulation or generally accepted practices or guidelines in the relevant jurisdictions (including any laws regarding the export of data or software to and from the United States or other relevant countries).
        4.3 You agree that if you use the SDK to develop applications for general public users, you will protect the privacy and legal rights of those users. If the users provide you with user names, passwords, or other login information or personal information, you must make the users aware that the information will be available to your application, and you must provide legally adequate privacy notice and protection for those users. If your application stores personal or sensitive information provided by users, it must do so securely. If the user provides your application with Google Account information, your application may only use that information to access the user's Google Account when, and for the limited purposes for which, the user has given you permission to do so.
        4.4 You agree that you will not engage in any activity with the SDK, including the development or distribution of an application, that interferes with, disrupts, damages, or accesses in an unauthorized manner the servers, networks, or other properties or services of any third party including, but not limited to, Google or any mobile communications carrier.
        4.5 You agree that you are solely responsible for (and that Google has no responsibility to you or to any third party for) any data, content, or resources that you create, transmit or display through Android and/or applications for Android, and for the consequences of your actions (including any loss or damage which Google may suffer) by doing so.
        4.6 You agree that you are solely responsible for (and that Google has no responsibility to you or to any third party for) any breach of your obligations under the License Agreement, any applicable third party contract or Terms of Service, or any applicable law or regulation, and for the consequences (including any loss or damage which Google or any third party may suffer) of any such breach.


        5. Your Developer Credentials

        5.1 You agree that you are responsible for maintaining the confidentiality of any developer credentials that may be issued to you by Google or which you may choose yourself and that you will be solely responsible for all applications that are developed under your developer credentials.


        6. Privacy and Information

        6.1 In order to continually innovate and improve the SDK, Google may collect certain usage statistics from the software including but not limited to a unique identifier, associated IP address, version number of the software, and information on which tools and/or services in the SDK are being used and how they are being used. Before any of this information is collected, the SDK will notify you and seek your consent. If you withhold consent, the information will not be collected.
        6.2 The data collected is examined in the aggregate to improve the SDK and is maintained in accordance with Google's Privacy Policy, which is located at the following URL: https://policies.google.com/privacy
        6.3 Anonymized and aggregated sets of the data may be shared with Google partners to improve the SDK.

        7. Third Party Applications

        7.1 If you use the SDK to run applications developed by a third party or that access data, content or resources provided by a third party, you agree that Google is not responsible for those applications, data, content, or resources. You understand that all data, content or resources which you may access through such third party applications are the sole responsibility of the person from which they originated and that Google is not liable for any loss or damage that you may experience as a result of the use or access of any of those third party applications, data, content, or resources.
        7.2 You should be aware the data, content, and resources presented to you through such a third party application may be protected by intellectual property rights which are owned by the providers (or by other persons or companies on their behalf). You may not modify, rent, lease, loan, sell, distribute or create derivative works based on these data, content, or resources (either in whole or in part) unless you have been specifically given permission to do so by the relevant owners.
        7.3 You acknowledge that your use of such third party applications, data, content, or resources may be subject to separate terms between you and the relevant third party. In that case, the License Agreement does not affect your legal relationship with these third parties.


        8. Using Android APIs

        8.1 Google Data APIs
        8.1.1 If you use any API to retrieve data from Google, you acknowledge that the data may be protected by intellectual property rights which are owned by Google or those parties that provide the data (or by other persons or companies on their behalf). Your use of any such API may be subject to additional Terms of Service. You may not modify, rent, lease, loan, sell, distribute or create derivative works based on this data (either in whole or in part) unless allowed by the relevant Terms of Service.
        8.1.2 If you use any API to retrieve a user's data from Google, you acknowledge and agree that you shall retrieve data only with the user's explicit consent and only when, and for the limited purposes for which, the user has given you permission to do so. If you use the Android Recognition Service API, documented at the following URL: https://developer.android.com/reference/android/speech/RecognitionService, as updated from time to time, you acknowledge that the use of the API is subject to the Data Processing Addendum for Products where Google is a Data Processor, which is located at the following URL: https://privacy.google.com/businesses/gdprprocessorterms/, as updated from time to time. By clicking to accept, you hereby agree to the terms of the Data Processing Addendum for Products where Google is a Data Processor.



        9. Terminating this License Agreement

        9.1 The License Agreement will continue to apply until terminated by either you or Google as set out below.
        9.2 If you want to terminate the License Agreement, you may do so by ceasing your use of the SDK and any relevant developer credentials.
        9.3 Google may at any time, terminate the License Agreement with you if:
        (A) you have breached any provision of the License Agreement; or
        (B) Google is required to do so by law; or
        (C) the partner with whom Google offered certain parts of SDK (such as APIs) to you has terminated its relationship with Google or ceased to offer certain parts of the SDK to you; or
        (D) Google decides to no longer provide the SDK or certain parts of the SDK to users in the country in which you are resident or from which you use the service, or the provision of the SDK or certain SDK services to you by Google is, in Google's sole discretion, no longer commercially viable.
        9.4 When the License Agreement comes to an end, all of the legal rights, obligations and liabilities that you and Google have benefited from, been subject to (or which have accrued over time whilst the License Agreement has been in force) or which are expressed to continue indefinitely, shall be unaffected by this cessation, and the provisions of paragraph 14.7 shall continue to apply to such rights, obligations and liabilities indefinitely.


        10. DISCLAIMER OF WARRANTIES

        10.1 YOU EXPRESSLY UNDERSTAND AND AGREE THAT YOUR USE OF THE SDK IS AT YOUR SOLE RISK AND THAT THE SDK IS PROVIDED "AS IS" AND "AS AVAILABLE" WITHOUT WARRANTY OF ANY KIND FROM GOOGLE.
        10.2 YOUR USE OF THE SDK AND ANY MATERIAL DOWNLOADED OR OTHERWISE OBTAINED THROUGH THE USE OF THE SDK IS AT YOUR OWN DISCRETION AND RISK AND YOU ARE SOLELY RESPONSIBLE FOR ANY DAMAGE TO YOUR COMPUTER SYSTEM OR OTHER DEVICE OR LOSS OF DATA THAT RESULTS FROM SUCH USE.
        10.3 GOOGLE FURTHER EXPRESSLY DISCLAIMS ALL WARRANTIES AND CONDITIONS OF ANY KIND, WHETHER EXPRESS OR IMPLIED, INCLUDING, BUT NOT LIMITED TO THE IMPLIED WARRANTIES AND CONDITIONS OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NON-INFRINGEMENT.


        11. LIMITATION OF LIABILITY
        
        11.1 YOU EXPRESSLY UNDERSTAND AND AGREE THAT GOOGLE, ITS SUBSIDIARIES AND AFFILIATES, AND ITS LICENSORS SHALL NOT BE LIABLE TO YOU UNDER ANY THEORY OF LIABILITY FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, CONSEQUENTIAL OR EXEMPLARY DAMAGES THAT MAY BE INCURRED BY YOU, INCLUDING ANY LOSS OF DATA, WHETHER OR NOT GOOGLE OR ITS REPRESENTATIVES HAVE BEEN ADVISED OF OR SHOULD HAVE BEEN AWARE OF THE POSSIBILITY OF ANY SUCH LOSSES ARISING.


        12. Indemnification
        
        12.1 To the maximum extent permitted by law, you agree to defend, indemnify and hold harmless Google, its affiliates and their respective directors, officers, employees and agents from and against any and all claims, actions, suits or proceedings, as well as any and all losses, liabilities, damages, costs and expenses (including reasonable attorneys fees) arising out of or accruing from (a) your use of the SDK, (b) any application you develop on the SDK that infringes any copyright, trademark, trade secret, trade dress, patent or other intellectual property right of any person or defames any person or violates their rights of publicity or privacy, and (c) any non-compliance by you with the License Agreement.


        13. Changes to the License Agreement
        13.1 Google may make changes to the License Agreement as it distributes new versions of the SDK. When these changes are made, Google will make a new version of the License Agreement available on the website where the SDK is made available.


        14. General Legal Terms
        
        14.1 The License Agreement constitutes the whole legal agreement between you and Google and governs your use of the SDK (excluding any services which Google may provide to you under a separate written agreement), and completely replaces any prior agreements between you and Google in relation to the SDK.
        14.2 You agree that if Google does not exercise or enforce any legal right or remedy which is contained in the License Agreement (or which Google has the benefit of under any applicable law), this will not be taken to be a formal waiver of Google's rights and that those rights or remedies will still be available to Google.
        14.3 If any court of law, having the jurisdiction to decide on this matter, rules that any provision of the License Agreement is invalid, then that provision will be removed from the License Agreement without affecting the rest of the License Agreement. The remaining provisions of the License Agreement will continue to be valid and enforceable.
        14.4 You acknowledge and agree that each member of the group of companies of which Google is the parent shall be third party beneficiaries to the License Agreement and that such other companies shall be entitled to directly enforce, and rely upon, any provision of the License Agreement that confers a benefit on (or rights in favor of) them. Other than this, no other person or company shall be third party beneficiaries to the License Agreement.
        14.5 EXPORT RESTRICTIONS. THE SDK IS SUBJECT TO UNITED STATES EXPORT LAWS AND REGULATIONS. YOU MUST COMPLY WITH ALL DOMESTIC AND INTERNATIONAL EXPORT LAWS AND REGULATIONS THAT APPLY TO THE SDK. THESE LAWS INCLUDE RESTRICTIONS ON DESTINATIONS, END USERS AND END USE.
        14.6 The rights granted in the License Agreement may not be assigned or transferred by either you or Google without the prior written approval of the other party. Neither you nor Google shall be permitted to delegate their responsibilities or obligations under the License Agreement without the prior written approval of the other party.
        14.7 The License Agreement, and your relationship with Google under the License Agreement, shall be governed by the laws of the State of California without regard to its conflict of laws provisions. You and Google agree to submit to the exclusive jurisdiction of the courts located within the county of Santa Clara, California to resolve any legal matter arising from the License Agreement. Notwithstanding this, you agree that Google shall still be allowed to apply for injunctive remedies (or an equivalent type of urgent legal relief) in any jurisdiction.


        July 27, 2021
    """.trimIndent()

}